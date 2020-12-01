package com.github.windchopper.tools.everquest.log.parser.log

import java.time.LocalDateTime

abstract class AnyRecord(val dateTime: LocalDateTime)

class ZoneEnterRecord(dateTime: LocalDateTime): AnyRecord(dateTime) {


}