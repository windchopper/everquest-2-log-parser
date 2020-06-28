package com.github.windchopper.tools.eq.lp.log

import com.github.windchopper.tools.eq.lp.Application
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped @PartBuilder class DateTimeBuilder: LogRecordPartBuilder<DateTime?> {

    override fun regularExpression(): String {
        return "^\\((?<dateTimeSeconds>\\d{10})\\)\\[(?<dateTimeFormattedText>)\\]"
    }

    override fun toString(): String {
        return Application.messages["name.wind.tools.eq2.lp.logRecordPartBuilder.dateTime.description"]!!
    }

}