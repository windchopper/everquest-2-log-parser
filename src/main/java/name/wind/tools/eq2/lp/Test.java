package name.wind.tools.eq2.lp;

import name.wind.tools.eq2.lp.log.LogRecordBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Test {

    public static void main(String... args) throws IOException {
        LogRecordBuilder logRecordBuilder = new LogRecordBuilder();
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("D:\\Users\\Wind\\EQ2\\logs\\Thurgadin\\eq2log_Breezzie.2016.10.04.txt"))) {
            logRecordBuilder.read(bufferedReader);
        }
    }

}
