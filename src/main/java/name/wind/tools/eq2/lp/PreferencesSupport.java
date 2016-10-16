package name.wind.tools.eq2.lp;

import name.wind.common.preferences.DefaultPreferencesEntry;
import name.wind.common.preferences.PreferencesEntry;
import name.wind.common.preferences.PreferencesEntryType;
import name.wind.common.preferences.store.DefaultFlatPreferencesStorage;
import name.wind.common.preferences.store.PreferencesStorage;

import java.io.File;

public interface PreferencesSupport {

    PreferencesStorage<String> preferencesStorage = new DefaultFlatPreferencesStorage("/name/wind/tools/eq2/lp");

    String PREFERENCES_ENTRY_NAME__OPEN_FILE_INITIAL_DIRECTORY = "openFileInitialDirectory";

    PreferencesEntry<File> openFileInitialDirectoryPreferencesEntry = new DefaultPreferencesEntry<>(
        preferencesStorage,
        PREFERENCES_ENTRY_NAME__OPEN_FILE_INITIAL_DIRECTORY,
        PreferencesEntryType.fileType);

    String PREFERENCES_ENTRY_NAME__LOG_REGEXP = "logRegexp";

    PreferencesEntry<String> logRegexp = new DefaultPreferencesEntry<>(
        preferencesStorage,
        PREFERENCES_ENTRY_NAME__LOG_REGEXP,
        PreferencesEntryType.stringType);

}
