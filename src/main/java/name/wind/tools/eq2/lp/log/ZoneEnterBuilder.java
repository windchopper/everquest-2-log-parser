package name.wind.tools.eq2.lp.log;

import name.wind.tools.eq2.lp.I18nSupport;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped @PartBuilder public class ZoneEnterBuilder implements LogRecordPartBuilder<ZoneEnter>, I18nSupport {

    @Override public String regularExpression() {
        return "\\QYou have entered\\E\\s(?<zone>.*)\\.$";
    }

    @Override public String toString() {
        return bundle.getString("name.wind.tools.eq2.lp.logRecordPartBuilder.zoneEnter.description");
    }

}
