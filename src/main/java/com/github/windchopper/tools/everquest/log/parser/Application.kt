package com.github.windchopper.tools.everquest.log.parser

import com.github.windchopper.common.fx.cdi.ResourceBundleLoad
import com.github.windchopper.common.fx.cdi.form.StageFormLoad
import com.github.windchopper.common.preferences.*
import com.github.windchopper.common.preferences.CompositePreferencesStorage.Mediator
import com.github.windchopper.common.preferences.types.FlatType
import com.github.windchopper.common.util.ClassPathResource
import javafx.stage.Stage
import org.jboss.weld.environment.se.Weld
import org.jboss.weld.environment.se.WeldContainer
import java.io.File
import java.time.Duration
import java.util.*
import java.util.prefs.Preferences
import java.util.regex.Pattern
import javax.json.Json

class Application: javafx.application.Application() {

    companion object {

        private val resourceBundle = ResourceBundle.getBundle("com.github.windchopper.tools.everquest.log.parser.i18n.messages")

        val messages = resourceBundle.keySet()
            .map { it to resourceBundle.getString(it) }
            .toMap()

        private val defaultBufferLifetime = Duration.ofMinutes(1)

        private val initialPreferencesStorage = JsonPreferencesStorage(Json.createReader(
            ClassPathResource("com/github/windchopper/tools/everquest/log/parser/initialPreferences.json").stream())
                .use { reader ->
                    reader.readObject()
                })

        private val realPreferencesStorage = PlatformPreferencesStorage(
            Preferences.userRoot().node("com/github/windchopper/tools/everquest/log/parser"))

        private val preferencesStorage = CompositePreferencesStorage(listOf(
            Mediator(initialPreferencesStorage, 1, 0, false),
            Mediator(realPreferencesStorage, 2, 1, true)))

        const val FXML__EVENT_BROWSER_STAGE = "com/github/windchopper/tools/everquest/log/parser/eventBrowserStage.fxml"
        const val FXML__SELECT_LOG_EVENT_BUILDER_STAGE = "com/github/windchopper/tools/everquest/log/parser/selectLogEventBuilderStage.fxml"

        val openFileInitialDirectory = PreferencesEntry(preferencesStorage, "openFileInitialDirectory", FlatType(::File, File::getAbsolutePath), defaultBufferLifetime)
        val logRegexp = PreferencesEntry(preferencesStorage, "logRegexp", FlatType(Pattern::compile, Pattern::pattern), defaultBufferLifetime)

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
