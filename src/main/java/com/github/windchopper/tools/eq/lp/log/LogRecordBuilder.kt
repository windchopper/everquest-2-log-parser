package com.github.windchopper.tools.eq.lp.log

import com.github.windchopper.tools.eq.lp.Application
import java.io.BufferedReader
import java.io.IOException
import java.util.*
import java.util.regex.Pattern

class LogRecordBuilder {
    private val logRecordPattern: Pattern

    @Throws(IOException::class)
    fun read(reader: BufferedReader) {
        val logRecordMatcher = logRecordPattern.matcher("")
        var currentLine: String
        val committedResults: Deque<String> = LinkedList()
        var previousResult: String? = null
        while (reader.readLine().also { currentLine = it } != null) {
            if (currentLine.isEmpty()) {
                continue
            }
            logRecordMatcher.reset(currentLine)
            previousResult = if (logRecordMatcher.matches()) {
                if (previousResult != null) {
                    committedResults.addLast(currentLine)
                }
                currentLine
            } else {
                """
     $previousResult
     $currentLine
     """.trimIndent()
            }
        }
    }

    companion object {
        private const val REGEXP__LOG_RECORD = "(?sm)^\\((?<timestamp>\\d{10})\\)\\[.+\\]\\s(" +
            "(\\QYou have entered\\E\\s(?<zone>.+)\\.)|" +
            "(?<notParsed>.+)" +
            ")\\z"
    }

    init {
        logRecordPattern = Optional.ofNullable(Application.logRegexp.load())
            .orElseGet {
                val pattern = Pattern.compile(REGEXP__LOG_RECORD)
                Application.logRegexp.save(pattern)
                pattern
            }
    }
}