package name.wind.tools.eq2.lp.log;

public class ZoneEnter implements LogRecordPart {

    private final String zone;

    public ZoneEnter(String zone) {
        this.zone = zone;
    }

    public String zone() {
        return zone;
    }

}
