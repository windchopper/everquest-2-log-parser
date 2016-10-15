package name.wind.tools.eq2.lp.log;

import name.wind.tools.eq2.lp.I18nSupport;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped @PartBuilder public class DateTimeBuilder implements LogRecordPartBuilder<DateTime>, I18nSupport {

    @Override public String regularExpression() {
        return "^\\((?<dateTimeSeconds>\\d{10})\\)\\[(?<dateTimeFormattedText>)\\]";
    }

    @Override public String toString() {
        return bundle.getString("name.wind.tools.eq2.lp.logRecordPartBuilder.dateTime.description");
    }

}
