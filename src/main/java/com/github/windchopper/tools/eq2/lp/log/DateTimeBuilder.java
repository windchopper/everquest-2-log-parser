package com.github.windchopper.tools.eq2.lp.log;

import com.github.windchopper.tools.eq2.lp.Globals;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped @PartBuilder public class DateTimeBuilder implements LogRecordPartBuilder<DateTime> {

    @Override public String regularExpression() {
        return "^\\((?<dateTimeSeconds>\\d{10})\\)\\[(?<dateTimeFormattedText>)\\]";
    }

    @Override public String toString() {
        return Globals.I18n.bundle.getString("name.wind.tools.eq2.lp.logRecordPartBuilder.dateTime.description");
    }

}
