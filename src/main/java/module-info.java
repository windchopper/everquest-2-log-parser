module windchopper.tools.everquest.log.parser {

    opens com.github.windchopper.tools.everquest.log.parser;
    opens com.github.windchopper.tools.everquest.log.parser.forms;
    opens com.github.windchopper.tools.everquest.log.parser.i18n;
    opens com.github.windchopper.tools.everquest.log.parser.log;
    opens com.github.windchopper.tools.everquest.log.parser.ui;

    requires kotlin.stdlib;
    requires kotlin.reflect;

    requires java.prefs;
    requires java.json;

    requires javafx.controls;
    requires javafx.fxml;

    requires jakarta.annotation;
    requires jakarta.inject;
    requires jakarta.cdi;

    requires weld.se.core;
    requires weld.environment.common;

    requires windchopper.common.fx;
    requires windchopper.common.fx.cdi;
    requires windchopper.common.io;
    requires windchopper.common.preferences;
    requires windchopper.common.util;

}