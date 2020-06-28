package com.github.windchopper.tools.eq.lp.log

import com.github.windchopper.tools.eq.lp.Application
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped @PartBuilder class ZoneEnterBuilder: LogRecordPartBuilder<ZoneEnter?> {

    override fun regularExpression(): String? {
        return "\\QYou have entered\\E\\s(?<zone>.*)\\.$"
    }

    override fun toString(): String {
        return Application.messages["name.wind.tools.eq2.lp.logRecordPartBuilder.zoneEnter.description"]!!
    }

}