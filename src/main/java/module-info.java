module windchopper.tools.everquest.log.parser {

    opens com.github.windchopper.tools.eq.lp;
    opens com.github.windchopper.tools.eq.lp.i18n;
    opens com.github.windchopper.tools.eq.lp.log;

    requires java.prefs;

    requires javafx.controls;
    requires javafx.fxml;

    requires weld.se.core;
    requires weld.environment.common;

    requires windchopper.common.fx;
    requires windchopper.common.io;
    requires windchopper.common.preferences;
    requires windchopper.common.util;

}