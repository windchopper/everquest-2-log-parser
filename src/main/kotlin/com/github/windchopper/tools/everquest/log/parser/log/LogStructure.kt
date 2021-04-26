@file:Suppress("HasPlatformType", "RemoveCurlyBracesFromTemplate")

package com.github.windchopper.tools.everquest.log.parser.log

import com.github.windchopper.common.preferences.entries.BufferedEntry
import com.github.windchopper.common.preferences.entries.CompositeEntry
import com.github.windchopper.tools.everquest.log.parser.Application.Companion.preferencesBufferLifetime
import com.github.windchopper.tools.everquest.log.parser.Application.Companion.preferencesComposition
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.ConcurrentSkipListMap
import java.util.concurrent.CopyOnWriteArraySet
import java.util.regex.Matcher
import java.util.stream.Collectors
import java.util.stream.Collectors.toCollection
import java.util.stream.Stream
import kotlin.reflect.full.memberProperties

abstract class LogEvent(var dateTime: LocalDateTime) {

    override fun toString(): String {
        return with(this::class) {
            "${simpleName}[${memberProperties.joinToString("; ") { property ->
                "${property.name} = ${property.getter.call(this@LogEvent)}"
            }}]"
        }
    }

}

class AnyEvent(dateTime: LocalDateTime, var text: String): LogEvent(dateTime)

class ZoneEnterEvent(dateTime: LocalDateTime, val zone: String): LogEvent(dateTime)

abstract class CharacterEvent(dateTime: LocalDateTime, val character: String): LogEvent(dateTime)

class CharacterLoggedInEvent(dateTime: LocalDateTime, val relation: String, character: String): CharacterEvent(dateTime, character)

class CharacterSkillAppliedEvent(dateTime: LocalDateTime, character: String, val skill: String, val action: String, val rest: String): CharacterEvent(dateTime, character)

class CharacterSayEvent(dateTime: LocalDateTime, character: String, val listener: String?, val message: String): CharacterEvent(dateTime, character)

abstract class LogEventFactory<EventType: LogEvent>

class AnyEventFactory: LogEventFactory<AnyEvent>() {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("EE MMM ppd HH:mm:ss yyyy", Locale.ENGLISH)
    private val anyPattern = BufferedEntry(preferencesBufferLifetime, CompositeEntry(preferencesComposition, "pattern/any", PatternType()))

    fun extract(text: String): AnyEvent? {
        return anyPattern.load().value.matcher(text).takeIf(Matcher::matches)
            ?.let { matcher -> AnyEvent(
                LocalDateTime.parse(matcher.group("dateTime"), dateTimeFormatter),
                matcher.group("text"))
            }
    }

}

abstract class SpecificEventFactory<EventType: LogEvent>: LogEventFactory<EventType>() {

    abstract fun extract(rawEvent: AnyEvent): EventType?

}

class ZoneEnterEventFactory: SpecificEventFactory<ZoneEnterEvent>() {

    private val zoneEnterPattern = BufferedEntry(preferencesBufferLifetime, CompositeEntry(preferencesComposition, "pattern/zoneEnter", PatternType()))

    override fun extract(rawEvent: AnyEvent): ZoneEnterEvent? {
        return zoneEnterPattern.load().value.matcher(rawEvent.text).takeIf(Matcher::matches)
            ?.let { matcher ->
                ZoneEnterEvent(rawEvent.dateTime, matcher.group("zone"))
            }
    }

}

class CharacterLoggedInEventFactory: SpecificEventFactory<CharacterLoggedInEvent>() {

    private val characterLoggedInPattern = BufferedEntry(preferencesBufferLifetime, CompositeEntry(preferencesComposition, "pattern/characterLoggedIn", PatternType()))

    override fun extract(rawEvent: AnyEvent): CharacterLoggedInEvent? {
        return characterLoggedInPattern.load().value.matcher(rawEvent.text).takeIf(Matcher::matches)
            ?.let { matcher ->
                CharacterLoggedInEvent(rawEvent.dateTime, matcher.group("relation"), matcher.group("character"))
            }
    }

}

class CharacterSkillAppliedEventFactory: SpecificEventFactory<CharacterSkillAppliedEvent>() {

    private val characterSkillAppliedPattern = BufferedEntry(preferencesBufferLifetime, CompositeEntry(preferencesComposition, "pattern/characterSkillApplied", PatternType()))

    override fun extract(rawEvent: AnyEvent): CharacterSkillAppliedEvent? {
        return characterSkillAppliedPattern.load().value.matcher(rawEvent.text).takeIf(Matcher::matches)
            ?.let { matcher ->
                CharacterSkillAppliedEvent(rawEvent.dateTime, matcher.group("character")?:"YOU", matcher.group("skill"), matcher.group("action"), matcher.group("rest"))
            }
    }

}

class CharacterSayEventFactory: SpecificEventFactory<CharacterSayEvent>() {

    private val characterSayPattern = BufferedEntry(preferencesBufferLifetime, CompositeEntry(preferencesComposition, "pattern/characterSay", PatternType()))

    override fun extract(rawEvent: AnyEvent): CharacterSayEvent? {
        return characterSayPattern.load().value.matcher(rawEvent.text).takeIf(Matcher::matches)
            ?.let { matcher ->
                CharacterSayEvent(rawEvent.dateTime, matcher.group("character"), matcher.group("listener"), matcher.group("message"))
            }
    }

}

class EventParser {

    companion object {

        private val eventFactory = AnyEventFactory()
        private val specificEventFactories = listOf(
            CharacterSayEventFactory(),
            CharacterSkillAppliedEventFactory(),
            CharacterLoggedInEventFactory(),
            ZoneEnterEventFactory())

    }

    private var lastParsedEvent: AnyEvent? = null
    private val skippedLines: MutableList<String> = LinkedList()

    fun parseRaw(lines: Stream<String>): List<AnyEvent> {
        val events = LinkedList<AnyEvent>()

        lines.forEach { line ->
            val event = eventFactory.extract(line)

            if (event == null) {
                if (line.isNotBlank()) skippedLines.add(line)
            } else {
                if (skippedLines.isNotEmpty()) {
                    lastParsedEvent?.let { presentLastParsedEvent ->
                        presentLastParsedEvent.text += System.lineSeparator()
                        presentLastParsedEvent.text += skippedLines.joinToString(System.lineSeparator())
                    }

                    skippedLines.clear()
                }

                lastParsedEvent = event
                events.add(event)
            }
        }

        return events
    }

    fun parseSpecific(rawEvents: Collection<AnyEvent>): Map<LocalDateTime, Collection<LogEvent>> {
        return rawEvents.parallelStream()
            .map(this::parseSpecific)
            .collect(Collectors.groupingBy(LogEvent::dateTime, ::ConcurrentSkipListMap,
                toCollection(::CopyOnWriteArraySet)))
    }

    fun parseSpecific(rawEvent: AnyEvent): LogEvent {
        return specificEventFactories.mapNotNull { factory -> factory.extract(rawEvent) }
            .firstOrNull()?:rawEvent
    }

}