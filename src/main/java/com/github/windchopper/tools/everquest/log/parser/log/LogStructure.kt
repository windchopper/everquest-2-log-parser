@file:Suppress("HasPlatformType", "RemoveCurlyBracesFromTemplate")

package com.github.windchopper.tools.everquest.log.parser.log

import com.github.windchopper.common.preferences.PreferencesEntry
import com.github.windchopper.common.preferences.types.FlatType
import com.github.windchopper.tools.everquest.log.parser.Application.Companion.defaultBufferLifetime
import com.github.windchopper.tools.everquest.log.parser.Application.Companion.preferencesStorage
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

abstract class LogEvent(val dateTime: LocalDateTime)

interface AppendableLogEvent {

    fun append(text: String)

}

class NotSupportedEvent(dateTime: LocalDateTime, val text: String): LogEvent(dateTime) {

    override fun toString(): String {
        return "${javaClass.simpleName}(dateTime: ${dateTime}; text: '${text}')"
    }

}

class ZoneEnterEvent(dateTime: LocalDateTime, val zone: String): LogEvent(dateTime) {

    override fun toString(): String {
        return "${javaClass.simpleName}(dateTime: ${dateTime}; zone: '${zone}')"
    }

}

abstract class CharacterEvent(dateTime: LocalDateTime, val character: String): LogEvent(dateTime)

class CharacterLoggedInEvent(dateTime: LocalDateTime, val relation: String, character: String): CharacterEvent(dateTime, character) {

    override fun toString(): String {
        return "${javaClass.simpleName}(dateTime: ${dateTime}; relation: '${relation}'; character: '${character}')"
    }

}

class CharacterSkillAppliedEvent(dateTime: LocalDateTime, character: String, val skill: String, val action: String, val rest: String): CharacterEvent(dateTime, character) {

    override fun toString(): String {
        return "${javaClass.simpleName}(dateTime: ${dateTime}; character: '${character}'; skill: '${skill}'; action: '${action}'; rest: '${rest}')"
    }

}

abstract class LogEventFactory {

    companion object {

        @JvmStatic protected val patternPreferencesStorage = preferencesStorage.child("pattern")
        @JvmStatic protected val patternType = FlatType(Pattern::compile, Pattern::pattern)

        @JvmStatic protected val dateTimeFormatter = DateTimeFormatter.ofPattern("EE MMM ppd HH:mm:ss yyyy", Locale.ENGLISH)

    }

    abstract fun extract(text: String): LogEvent?

}

class NotSupportedEventFactory: LogEventFactory() {

    companion object {

        private val notSupportedPattern = PreferencesEntry(patternPreferencesStorage, "notSupported", patternType, defaultBufferLifetime)

    }

    override fun extract(text: String): LogEvent? {
        return notSupportedPattern.load().matcher(text).takeIf(Matcher::matches)
            ?.let { matcher -> NotSupportedEvent(
                LocalDateTime.parse(matcher.group("dateTime"), dateTimeFormatter),
                matcher.group("text"))
            }
    }

}

class ZoneEnterEventFactory: LogEventFactory() {

    companion object {

        private val zoneEnterPattern = PreferencesEntry(patternPreferencesStorage, "zoneEnter", patternType, defaultBufferLifetime)

    }

    override fun extract(text: String): ZoneEnterEvent? {
        return zoneEnterPattern.load().matcher(text).takeIf(Matcher::matches)
            ?.let { matcher -> ZoneEnterEvent(
                LocalDateTime.parse(matcher.group("dateTime"), dateTimeFormatter),
                matcher.group("zone"))
            }
    }

}

class CharacterLoggedInEventFactory: LogEventFactory() {

    companion object {

        private val characterLoggedInPattern = PreferencesEntry(patternPreferencesStorage, "characterLoggedIn", patternType, defaultBufferLifetime)

    }

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

    companion object {

        private val characterSkillAppliedPattern = PreferencesEntry(patternPreferencesStorage, "characterSkillApplied", patternType, defaultBufferLifetime)

    }

    override fun extract(text: String): CharacterSkillAppliedEvent? {
        return characterSkillAppliedPattern.load().matcher(text).takeIf(Matcher::matches)
            ?.let { matcher -> CharacterSkillAppliedEvent(
                LocalDateTime.parse(matcher.group("dateTime"), dateTimeFormatter),
                matcher.group("character"),
                matcher.group("skill"),
                matcher.group("action"),
                matcher.group("rest"))
            }
    }

}

fun main(vararg args: String) {
    val eventFactories = listOf(
        ZoneEnterEventFactory(),
        CharacterLoggedInEventFactory(),
        CharacterSkillAppliedEventFactory(),
        NotSupportedEventFactory())

    var count = 0

    Files.newBufferedReader(Paths.get("D:\\Users\\Wind\\EQ2\\logs\\Thurgadin\\eq2log_Papagayo.txt"), StandardCharsets.UTF_8)
        .use { reader ->
            var line: String?

            do {
                line = reader.readLine()
                line?.let { nonEmptyLine ->
                    var event: LogEvent? = null

                    for (factory in eventFactories) {
                        event = factory.extract(nonEmptyLine)

                        if (event != null) {
                            count++
                            if (event !is NotSupportedEvent) {
                                // println(event)
                                break
                            }
                        }
                    }

                    if (event == null) {
                        println("NOT EVENT: ${line}")
                    }
                }
            } while (
                line != null)
        }

    println("Events parsed: ${count}")
}