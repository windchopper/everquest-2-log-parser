package com.github.windchopper.tools.eq2.lp;

import com.github.windchopper.tools.eq2.lp.log.LogRecordBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatterBuilder;

public class Test {

    public static void main(String... args) throws IOException {
        LogRecordBuilder logRecordBuilder = new LogRecordBuilder();
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("D:\\Users\\Wind\\EQ2\\logs\\Thurgadin\\eq2log_Papagayo.2018.03.14.txt"))) {
            logRecordBuilder.read(bufferedReader);
        }
    }

}
