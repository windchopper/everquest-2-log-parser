package com.github.windchopper.tools.eq.lp.log

interface LogRecordPartBuilder<E: LogRecordPart?> {
    fun regularExpression(): String?
}