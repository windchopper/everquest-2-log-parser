package com.github.windchopper.tools.everquest.log.parser.log

import com.github.windchopper.common.preferences.PreferencesEntryFlatType
import java.util.regex.Pattern

class PatternType: PreferencesEntryFlatType<Pattern>(Pattern::compile, Pattern::pattern)