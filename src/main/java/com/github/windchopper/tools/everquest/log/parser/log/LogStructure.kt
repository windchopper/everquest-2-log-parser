@file:Suppress("HasPlatformType", "RemoveCurlyBracesFromTemplate")

package com.github.windchopper.tools.everquest.log.parser.log

import com.github.windchopper.common.preferences.BufferedPreferencesEntry
import com.github.windchopper.common.preferences.types.FlatType
import com.github.windchopper.tools.everquest.log.parser.Application.Companion.defaultBufferLifetime
import com.github.windchopper.tools.everquest.log.parser.Application.Companion.preferencesStorage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.reflect.full.memberProperties

abstract class LogEvent(val dateTime: LocalDateTime) {

    override fun toString(): String {
        return with(this::class) {
            "${simpleName}[${memberProperties.joinToString("; ") { property ->
                "${property.name} = ${property.getter.call(this@LogEvent)}"
            }}]"
        }
    }

}

class NotSupportedEvent(dateTime: LocalDateTime, val text: String): LogEvent(dateTime)

class ZoneEnterEvent(dateTime: LocalDateTime, val zone: String): LogEvent(dateTime)

abstract class CharacterEvent(dateTime: LocalDateTime, val character: String): LogEvent(dateTime)

class CharacterLoggedInEvent(dateTime: LocalDateTime, val relation: String, character: String): CharacterEvent(dateTime, character)

class CharacterSkillAppliedEvent(dateTime: LocalDateTime, character: String, val skill: String, val action: String, val rest: String): CharacterEvent(dateTime, character)

class CharacterSayEvent(dateTime: LocalDateTime, character: String, val listener: String?, val message: String): CharacterEvent(dateTime, character)

abstract class LogEventFactory {

    protected val dateTimeFormatter = DateTimeFormatter.ofPattern("EE MMM ppd HH:mm:ss yyyy", Locale.ENGLISH)
    protected val patternPreferencesStorage = preferencesStorage.child("pattern")
    protected val patternType = FlatType(Pattern::compile, Pattern::pattern)

    abstract fun extract(text: String): LogEvent?

}

class NotSupportedEventFactory: LogEventFactory() {

    private val notSupportedPattern = BufferedPreferencesEntry(patternPreferencesStorage, "notSupported", patternType, defaultBufferLifetime)

    override fun extract(text: String): LogEvent? {
        return notSupportedPattern.load().matcher(text).takeIf(Matcher::matches)
            ?.let { matcher -> NotSupportedEvent(
                LocalDateTime.parse(matcher.group("dateTime"), dateTimeFormatter),
                matcher.group("text"))
            }
    }

}

class ZoneEnterEventFactory: LogEventFactory() {

    private val zoneEnterPattern = BufferedPreferencesEntry(patternPreferencesStorage, "zoneEnter", patternType, defaultBufferLifetime)

    override fun extract(text: String): ZoneEnterEvent? {
        return zoneEnterPattern.load().matcher(text).takeIf(Matcher::matches)
            ?.let { matcher -> ZoneEnterEvent(
                LocalDateTime.parse(matcher.group("dateTime"), dateTimeFormatter),
                matcher.group("zone"))
            }
    }

}

class CharacterLoggedInEventFactory: LogEventFactory() {

    private val characterLoggedInPattern = BufferedPreferencesEntry(patternPreferencesStorage, "characterLoggedIn", patternType, defaultBufferLifetime)

    override fun extract(text: String): CharacterLoggedInEvent? {
        return characterLoggedInPattern.load().matcher(text).takeIf(Matcher::matches)
            ?.let { matcher -> CharacterLoggedInEvent(
                LocalDateTime.parse(matcher.group("dateTime"), dateTimeFormatter),
                matcher.group("relation"),
                matcher.group("character"))
            }
    }

}

class CharacterSkillAppliedEventFactory: LogEventFactory() {

    private val characterSkillAppliedPattern = BufferedPreferencesEntry(patternPreferencesStorage, "characterSkillApplied", patternType, defaultBufferLifetime)

    override fun extract(text: String): CharacterSkillAppliedEvent? {
        return characterSkillAppliedPattern.load().matcher(text).takeIf(Matcher::matches)
            ?.let { matcher -> CharacterSkillAppliedEvent(
                LocalDateTime.parse(matcher.group("dateTime"), dateTimeFormatter),
                matcher.group("character")?:"YOU",
                matcher.group("skill"),
                matcher.group("action"),
                matcher.group("rest"))
            }
    }

}

class CharacterSayEventFactory: LogEventFactory() {

    private val characterSayPattern = BufferedPreferencesEntry(patternPreferencesStorage, "characterSay", patternType, defaultBufferLifetime)

    override fun extract(text: String): CharacterSayEvent? {
        return characterSayPattern.load().matcher(text).takeIf(Matcher::matches)
            ?.let { matcher -> CharacterSayEvent(
                LocalDateTime.parse(matcher.group("dateTime"), dateTimeFormatter),
                matcher.group("character"),
                matcher.group("listener"),
                matcher.group("message"))
            }
    }

}
