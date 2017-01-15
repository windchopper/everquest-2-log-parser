package name.wind.tools.eq2.lp;

import name.wind.common.preferences.PlatformPreferencesStorage;
import name.wind.common.preferences.PreferencesEntry;
import name.wind.common.preferences.PreferencesStorage;
import name.wind.common.preferences.types.FlatType;

import java.io.File;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class Globals {

    public static class I18n {

        public static final ResourceBundle bundle = ResourceBundle.getBundle("name.wind.tools.eq2.lp.i18n.messages");

    }

    public static class Settings {

        private static final PreferencesStorage preferencesStorage = new PlatformPreferencesStorage(
            Preferences.userRoot().node("/name/wind/tools/eq2/lp"));

        public static final PreferencesEntry<File> openFileInitialDirectory = new PreferencesEntry<>(
            preferencesStorage, "openFileInitialDirectory", new FlatType<>(File::new, File::getAbsolutePath), ChronoUnit.FOREVER.getDuration());

        public static final PreferencesEntry<String> logRegexp = new PreferencesEntry<>(
            preferencesStorage, "logRegexp", new FlatType<>(string -> string, string -> string), ChronoUnit.FOREVER.getDuration());

    }

    public interface FXMLResources {

        String FXML__EVENT_BROWSER_STAGE = "/name/wind/tools/eq2/lp/eventBrowserStage.fxml";
        String FXML__SELECT_LOG_EVENT_BUILDER_STAGE = "/name/wind/tools/eq2/lp/selectLogEventBuilderStage.fxml";

    }


}
