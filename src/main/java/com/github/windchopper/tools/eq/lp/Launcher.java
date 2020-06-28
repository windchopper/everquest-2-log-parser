package com.github.windchopper.tools.eq.lp;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import java.util.ResourceBundle;

public class Launcher extends Application {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("com.github.windchopper.tools.eq2.lp.i18n.messages");

    private Weld weld;
    private WeldContainer weldContainer;

    @Override public void init() throws Exception {
        super.init();
        weld = new Weld();
        weldContainer = weld.initialize();
    }

    @Override public void stop() throws Exception {
        weld.shutdown();
        super.stop();
    }

    @Override public void start(Stage primaryStage) throws Exception {
        BeanManager beanManager = weldContainer.getBeanManager();
        beanManager.fireEvent(
            new ResourceBundleLoading(bundle));
        beanManager.fireEvent(
            new FXMLResourceOpen(primaryStage, Globals.FXMLResources.FXML__EVENT_BROWSER_STAGE));
    }

    /*
     *
     */

    public static void main(String... args) {
        launch(args);
    }

}
