package name.wind.tools.eq2.lp;

import name.wind.common.preferences.PlatformPreferencesStorage;
import name.wind.common.preferences.PreferencesEntry;
import name.wind.common.preferences.PreferencesEntryType;
import name.wind.common.preferences.PreferencesStorage;
import name.wind.common.preferences.types.FlatType;

import java.io.File;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class Globals {

    public static class I18n {

        public static final ResourceBundle bundle = ResourceBundle.getBundle("name.wind.tools.eq2.lp.i18n.messages");

    }

    public static class Settings {

        private static final PreferencesEntryType<File> fileType = new FlatType<>(File::new, File::getAbsolutePath);
        private static final PreferencesEntryType<String> stringType = new FlatType<>(String::toString, String::toString);

        private static final Duration defaultLifetime = ChronoUnit.FOREVER.getDuration();

        private static final PreferencesStorage preferencesStorage = new PlatformPreferencesStorage(
            Preferences.userRoot().node("/name/wind/tools/eq2/lp"));

        /*
         *
         */

        public static final PreferencesEntry<File> openFileInitialDirectory = new PreferencesEntry<>(preferencesStorage, "openFileInitialDirectory", fileType, defaultLifetime);
        public static final PreferencesEntry<String> logRegexp = new PreferencesEntry<>(preferencesStorage, "logRegexp", stringType, defaultLifetime);

    }

    public static class FXMLResources {

        public static final String FXML__EVENT_BROWSER_STAGE = "/name/wind/tools/eq2/lp/eventBrowserStage.fxml";
        public static final String FXML__SELECT_LOG_EVENT_BUILDER_STAGE = "/name/wind/tools/eq2/lp/selectLogEventBuilderStage.fxml";

    }

}
