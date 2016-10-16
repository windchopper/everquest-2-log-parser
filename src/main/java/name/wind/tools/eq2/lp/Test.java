package name.wind.tools.eq2.lp;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String... args) throws IOException {
        long start = System.currentTimeMillis();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get("D:\\Users\\Wind\\EQ2\\logs\\Thurgadin\\eq2log_Breezzie.2016.10.04.txt"))) {
            Pattern pattern = Pattern.compile("^\\(\\d{10}\\)");
            Matcher matcher = pattern.matcher("");

            List<String> recordLines = new ArrayList<>();
            String line = reader.readLine();

            while (line != null) {
                line = line.trim();
                String nextLine = reader.readLine();

                if (line.length() > 0) {
                    recordLines.add(line);

                    if (nextLine != null) {
                        matcher.reset(nextLine);
                    }

                    if (nextLine == null || matcher.find()) {
                        if (recordLines.size() > 1) {
                            recordLines.forEach(System.out::println);
                        }
                        recordLines.clear();
                    }
                }

                line = nextLine;
            }
        }

        System.out.println("elapsed: " + (System.currentTimeMillis() - start));
    }

}
