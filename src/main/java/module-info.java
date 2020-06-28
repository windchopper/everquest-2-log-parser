module windchopper.tools.everquest.log.parser {

    opens com.github.windchopper.tools.eq.lp;
    opens com.github.windchopper.tools.eq.lp.i18n;
    opens com.github.windchopper.tools.eq.lp.log;

    requires kotlin.stdlib;
    requires kotlin.stdlib.jdk8;

    requires java.prefs;

    requires javafx.controls;
    requires javafx.fxml;

    requires jakarta.inject.api;
    requires jakarta.enterprise.cdi.api;

    requires weld.se.core;
    requires weld.environment.common;

    requires windchopper.common.fx;
    requires windchopper.common.fx.cdi;
    requires windchopper.common.io;
    requires windchopper.common.preferences;
    requires windchopper.common.util;

}