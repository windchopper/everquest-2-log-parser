package com.github.windchopper.tools.eq.lp.log;

import com.github.windchopper.tools.eq.lp.Globals;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogRecordBuilder {

    private static final String REGEXP__LOG_RECORD = "(?sm)^\\((?<timestamp>\\d{10})\\)\\[.+\\]\\s(" +
        "(\\QYou have entered\\E\\s(?<zone>.+)\\.)|" +
        "(?<notParsed>.+)" +
        ")\\z";

    private final Pattern logRecordPattern;

    public LogRecordBuilder() {
        logRecordPattern = Pattern.compile(
            Optional.of(Globals.Settings.logRegexp)
                .orElseGet(() -> {
                    Globals.Settings.logRegexp.accept(REGEXP__LOG_RECORD);
                    return Globals.Settings.logRegexp;
                })
                .get());
    }

    public void read(BufferedReader reader) throws IOException {
        Matcher logRecordMatcher = logRecordPattern.matcher("");

        String currentLine;

        Deque<String> committedResults = new LinkedList<>();
        String previousResult = null;

        while ((currentLine = reader.readLine()) != null) {
            if (currentLine.isEmpty()) {
                continue;
            }

            logRecordMatcher.reset(currentLine);

            if (logRecordMatcher.matches()) {
                if (previousResult != null) {
                    committedResults.addLast(currentLine);
                }

                previousResult = currentLine;
            } else {
                previousResult = previousResult + "\n" + currentLine;
            }
        }
    }

}
