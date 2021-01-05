@file:Suppress("HasPlatformType")

package com.github.windchopper.tools.everquest.log.parser

import com.github.windchopper.common.fx.cdi.ResourceBundleLoad
import com.github.windchopper.common.fx.cdi.form.StageFormLoad
import com.github.windchopper.common.preferences.entries.*
import com.github.windchopper.common.preferences.types.*
import com.github.windchopper.common.preferences.storages.*
import com.github.windchopper.common.util.ClassPathResource
import javafx.stage.Stage
import org.jboss.weld.environment.se.Weld
import org.jboss.weld.environment.se.WeldContainer
import java.time.Duration
import java.util.*
import java.util.prefs.Preferences

class Application: javafx.application.Application() {

    companion object {

        const val FXML__EVENT_BROWSER_STAGE = "com/github/windchopper/tools/everquest/log/parser/forms/eventBrowserStage.fxml"
        const val FXML__LOG_FILE_SELECTION_STAGE = "com/github/windchopper/tools/everquest/log/parser/forms/logFileSelectionStage.fxml"
        const val FXML__PARSE_PROGRESS_STAGE = "com/github/windchopper/tools/everquest/log/parser/forms/parseProgressStage.fxml"

        private val resourceBundle = ResourceBundle.getBundle("com.github.windchopper.tools.everquest.log.parser.i18n.messages")

        val messages = resourceBundle.keySet()
            .map { it to resourceBundle.getString(it) }
            .toMap()

        private val mainPreferencesStorage = PlatformStorage(
            Preferences.userRoot().node("com/github/windchopper/tools/everquest/log/parser"))
        private val initialPreferencesStorage = JsonStorage(
            ClassPathResource("com/github/windchopper/tools/everquest/log/parser/initialPreferences.json"))

        enum class PreferencesKey {
            INITIAL, MAIN
        }

        val preferencesComposition = CompositeEntry.StorageComposition(
            mapOf(PreferencesKey.MAIN to mainPreferencesStorage, PreferencesKey.INITIAL to initialPreferencesStorage))
                .loadFrom(PreferencesKey.MAIN)
                .loadFrom(PreferencesKey.INITIAL)
                .loadNewer()
                .propagateTo(PreferencesKey.MAIN)
                .propagateOlder()

        val preferencesBufferLifetime = Duration.ofMinutes(1)

        val openFileInitialDirectory = BufferedEntry(preferencesBufferLifetime,
            CompositeEntry(preferencesComposition, "openFileInitialDirectory", FileType()))

    }

    private lateinit var weld: Weld
    private lateinit var weldContainer: WeldContainer

    override fun init() {
        weld = Weld()
        weldContainer = weld.initialize()
    }

    override fun start(primaryStage: Stage) {
        with (weldContainer.beanManager) {
            fireEvent(ResourceBundleLoad(resourceBundle))
            fireEvent(StageFormLoad(ClassPathResource(FXML__EVENT_BROWSER_STAGE)) { primaryStage })
        }
    }

    override fun stop() {
        weld.shutdown()
    }

}
