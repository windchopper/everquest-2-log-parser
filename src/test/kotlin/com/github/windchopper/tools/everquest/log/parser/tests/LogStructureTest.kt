package com.github.windchopper.tools.everquest.log.parser.tests

import com.github.windchopper.tools.everquest.log.parser.log.*
import java.net.URI
import java.nio.file.FileSystems
import java.nio.file.Files
import java.util.*
import kotlin.test.Test

class LogStructureTest {

    private val eventParser = EventParser()

    @Test fun testLogFileRead() {
        val packedLogUri = URI.create("jar:" + javaClass
            .getResource("/eq2log_Papagayo.2021.01.16.txt.zip")
            .toURI())

        FileSystems.newFileSystem(packedLogUri, mapOf("create" to true)).use { fileSystem ->
            val rawEvents = eventParser.parseRaw(Files.lines(fileSystem.getPath("eq2log_Papagayo.2021.01.16.txt")))
            val parsedEvents = eventParser.parseSpecific(rawEvents)

            println("parsed events (s): ${parsedEvents.values
                .sumOf(Collection<LogEvent>::size)}")
        }
    }

}