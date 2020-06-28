package com.github.windchopper.tools.eq.lp.log;

import com.github.windchopper.tools.eq.lp.Globals;

@ApplicationScoped @PartBuilder public class ZoneEnterBuilder implements LogRecordPartBuilder<ZoneEnter> {

    @Override public String regularExpression() {
        return "\\QYou have entered\\E\\s(?<zone>.*)\\.$";
    }

    @Override public String toString() {
        return Globals.I18n.bundle.getString("name.wind.tools.eq2.lp.logRecordPartBuilder.zoneEnter.description");
    }

}
