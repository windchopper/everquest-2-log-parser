package name.wind.tools.eq2.lp.log;

import java.time.LocalDateTime;

public class DateTime implements LogRecordPart {

    private final LocalDateTime dateTime;

    public DateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime dateTime() {
        return dateTime;
    }

}
