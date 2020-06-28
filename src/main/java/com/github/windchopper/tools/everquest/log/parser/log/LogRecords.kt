package com.github.windchopper.tools.everquest.log.parser.log

import java.time.LocalDateTime

abstract class AnyRecord(val dateTime: LocalDateTime)

class UnknownRecord(dateTime: LocalDateTime, val text: String): AnyRecord(dateTime)

class ZoneEnterRecord(dateTime: LocalDateTime): AnyRecord(dateTime)