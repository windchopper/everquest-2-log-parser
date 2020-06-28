module windchopper.tools.everquest.log.parser {

    opens com.github.windchopper.tools.eq2.lp;
    opens com.github.windchopper.tools.eq2.lp.i18n;
    opens com.github.windchopper.tools.eq2.lp.log;

    requires javafx.controls;
    requires javafx.fxml;

    requires weld.se.core;
    requires weld.environment.common;

}