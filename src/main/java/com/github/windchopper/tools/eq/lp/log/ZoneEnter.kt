package com.github.windchopper.tools.eq.lp.log

class ZoneEnter(private val zone: String): LogRecordPart {
    fun zone(): String {
        return zone
    }

}