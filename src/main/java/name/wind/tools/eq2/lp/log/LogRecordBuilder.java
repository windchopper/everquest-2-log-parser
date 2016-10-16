package name.wind.tools.eq2.lp.log;

import name.wind.common.util.Value;
import name.wind.tools.eq2.lp.PreferencesSupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogRecordBuilder implements PreferencesSupport {

    private static final String REGEXP__GROUP = "\\?<(?<groupName>\\w+)>";
    private static final String REGEXP__LOG_RECORD = "(?sm)^\\((?<timestamp>\\d{10})\\)\\[.+\\]\\s(" +
        "(\\QYou have entered\\E\\s(?<zone>.+)\\.)|" +
        "(?<notParsed>.+)" +
        ")\\z";

    private final Pattern groupPattern;
    private final Pattern logRecordPattern;

    private final Set<String> logRecordPatternGroups = new HashSet<>();

    public LogRecordBuilder() {
        groupPattern = Pattern.compile(REGEXP__GROUP);
        logRecordPattern = Pattern.compile(
            Value.of(logRegexp)
                .ifNotPresentSupply(() -> {
                    logRegexp.accept(REGEXP__LOG_RECORD);
                    return logRegexp.get();
                })
                .get());

        for (Matcher matcher = groupPattern.matcher(logRecordPattern.pattern()); matcher.find(); ) {
            logRecordPatternGroups.add(matcher.group("groupName"));
        }
    }

    public void read(BufferedReader bufferedReader) throws IOException {
        Matcher logRecordMatcher = logRecordPattern.matcher("");
        Matcher reserveLogRecordMatcher = logRecordPattern.matcher("");

        String logRecordLine = bufferedReader.readLine();
        Map<String, String> logRecordMatcherGroups = null;
        List<String> logRecordLines = new ArrayList<>();

        long start = System.nanoTime();
        long count = 0;

        while (logRecordLine != null) {
            logRecordLine = logRecordLine.trim();
            String nextLogRecordLine = bufferedReader.readLine();
            boolean nextLogRecordLineMatches = false;

            if (logRecordLine.length() > 0) {
                logRecordLines.add(logRecordLine);

                if (nextLogRecordLine != null) {
                    logRecordMatcher.reset(nextLogRecordLine);
                    nextLogRecordLineMatches = logRecordMatcher.matches();
                }

                if (nextLogRecordLine == null || nextLogRecordLineMatches) {
                    if (logRecordLines.size() > 1 || logRecordMatcherGroups == null) {
                        if (logRecordMatcherGroups == null) {
                            logRecordMatcherGroups = new HashMap<>(logRecordPatternGroups.size());
                        } else {
                            logRecordMatcherGroups.clear();
                        }

                        reserveLogRecordMatcher.reset(
                            String.join("\n", logRecordLines));

                        if (reserveLogRecordMatcher.matches()) {
                            System.out.println(reserveLogRecordMatcher.group());

                            for (String group : logRecordPatternGroups) {
                                logRecordMatcherGroups.put(group, reserveLogRecordMatcher.group(group));
                            }

                            loadLogRecord(logRecordMatcherGroups);
                        } else {
                            throw new RuntimeException("oops");
                        }
                    } else if (logRecordLines.size() > 0) {
                        loadLogRecord(logRecordMatcherGroups);
                    }

                    logRecordLines.clear();
                }
            }

            logRecordLine = nextLogRecordLine;

            if (nextLogRecordLineMatches) {
                logRecordMatcherGroups.clear();

                for (String group : logRecordPatternGroups) {
                    logRecordMatcherGroups.put(group, logRecordMatcher.group(group));
                }
            }

            count++;
        }

        long total = System.nanoTime() - start;

        System.out.println();
        System.out.println("average logRecordLine: " + TimeUnit.NANOSECONDS.toMillis(total / count) + " ms");
        System.out.println("total: " + TimeUnit.NANOSECONDS.toMillis(total) + " ms");
    }

    public void loadLogRecord(Map<String, String> logRecordMatcherGroups) {
    }

}
