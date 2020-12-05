@file:Suppress("HasPlatformType")

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
import kotlin.collections.ArrayList

abstract class LogEvent(val dateTime: LocalDateTime)

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

class CharacterLoggedInEvent(dateTime: LocalDateTime, val relation: String, val name: String): LogEvent(dateTime) {

    override fun toString(): String {
        return "${javaClass.simpleName}(dateTime: ${dateTime}; relation: '${relation}'; name: '${name}')"
    }

}

abstract class LogEventFactory {

    companion object {

        @JvmStatic protected val patternPreferencesStorage = preferencesStorage.child("pattern")
        @JvmStatic protected val patternType = FlatType(Pattern::compile, Pattern::pattern)

        @JvmStatic protected val dateTimeFormatter = DateTimeFormatter.ofPattern("EE MMM ppd HH:mm:ss yyyy", Locale.ENGLISH)

    }

    abstract fun parse(text: String): LogEvent?

}

class NotSupportedEventFactory: LogEventFactory() {

    companion object {

        private val notSupportedPattern = PreferencesEntry(patternPreferencesStorage, "notSupported", patternType, defaultBufferLifetime)

    }

    override fun parse(text: String): LogEvent? {
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

    override fun parse(text: String): ZoneEnterEvent? {
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

    override fun parse(text: String): CharacterLoggedInEvent? {
        return characterLoggedInPattern.load().matcher(text).takeIf(Matcher::matches)
            ?.let { matcher -> CharacterLoggedInEvent(
                LocalDateTime.parse(matcher.group("dateTime"), dateTimeFormatter),
                matcher.group("relation"),
                matcher.group("name"))
            }
    }

}

fun main(vararg args: String) {
    val events = ArrayList<LogEvent>()
    val eventFactories = listOf(
        ZoneEnterEventFactory(),
        CharacterLoggedInEventFactory(),
        NotSupportedEventFactory())

    Files.newBufferedReader(Paths.get("D:\\Users\\Wind\\EQ2\\logs\\Thurgadin\\eq2log_Papagayo.txt"), StandardCharsets.UTF_8)
        .use { reader ->
            var line: String?

            do {
                line = reader.readLine()
                line?.let { nonEmptyLine ->
                    for (factory in eventFactories) {
                        val event = factory.parse(nonEmptyLine)

                        if (event != null) {
                            events.add(event)
                            break
                        }
                    }
                }
            } while (
                line != null)
        }

    for (event in events) {
        if (event is NotSupportedEvent) continue
        println(event)
    }
}