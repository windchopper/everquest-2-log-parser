package name.wind.tools.eq2.lp.log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

public class LogRecordBuilder {

    private final Map<String, Pattern> patternBuffer = new HashMap<>();
    private final List<LogRecordPartBuilder<?>> builders;

    private static class MatchTask extends RecursiveTask<Matcher> {

        private final String text;

        public MatchTask(String text) {
            this.text = text;
        }

        @Override protected Matcher compute() {
            return null;
        }

    }

    public LogRecordBuilder(List<LogRecordPartBuilder<?>> builders) {
        this.builders = builders;
    }

    public LogRecord build(ForkJoinPool pool, String logLine) {
        class IntHolder {
            int value;

            public IntHolder(int value) {
                this.value = value;
            }
        }



        for (IntHolder index = new IntHolder(0); index.value < logLine.length(); ) {


            builders.parallelStream()
                .filter(builder -> patternBuffer.computeIfAbsent(builder.regularExpression(), Pattern::compile).matcher(logLine).find(index.value))
                .findFirst();


            new RecursiveTask<Object>() {

                @Override
                protected Object compute() {
                    return null;
                }
            }.invoke();

        }

        return null;
    }

    public static void main(String... args) {
        List<String> samples = asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        Random random = new Random();

        samples.parallelStream()
            .filter(sample -> {
                System.out.println(sample);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ignored) {
                }
                return random.nextBoolean();
            })
            .findFirst()
            .ifPresent(sample -> System.out.println("found: " + sample));
    }

}
