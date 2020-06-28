package com.github.windchopper.tools.everquest.log.parser

import com.github.windchopper.common.fx.cdi.ResourceBundleLoad
import com.github.windchopper.common.fx.cdi.form.StageFormLoad
import com.github.windchopper.common.preferences.PlatformPreferencesStorage
import com.github.windchopper.common.preferences.PreferencesEntry
import com.github.windchopper.common.preferences.PreferencesStorage
import com.github.windchopper.common.preferences.types.FlatType
import com.github.windchopper.common.util.ClassPathResource
import javafx.stage.Stage
import org.jboss.weld.environment.se.Weld
import org.jboss.weld.environment.se.WeldContainer
import java.io.File
import java.time.Duration
import java.util.*
import java.util.function.Function
import java.util.function.Supplier
import java.util.prefs.Preferences
import java.util.regex.Pattern

class Application: javafx.application.Application() {

    companion object {

        private val resourceBundle = ResourceBundle.getBundle("com.github.windchopper.tools.everquest.log.parser.i18n.messages")

        val messages = resourceBundle.keySet()
            .map { it to resourceBundle.getString(it) }
            .toMap()

        private val defaultBufferLifetime = Duration.ofMinutes(1)
        private val preferencesStorage: PreferencesStorage = PlatformPreferencesStorage(Preferences.userRoot().node("com/github/windchopper/tools/everquest/log/parser"))

        const val FXML__EVENT_BROWSER_STAGE = "/com/github/windchopper/tools/everquest/log/parser/eventBrowserStage.fxml"
        const val FXML__SELECT_LOG_EVENT_BUILDER_STAGE = "/com/github/windchopper/tools/everquest/log/parser/selectLogEventBuilderStage.fxml"

        val openFileInitialDirectory = PreferencesEntry<File>(preferencesStorage, "openFileInitialDirectory", FlatType(Function { File(it) }, Function { it.absolutePath }), defaultBufferLifetime)
        val logRegexp = PreferencesEntry<Pattern>(preferencesStorage, "logRegexp", FlatType(Function { Pattern.compile(it) }, Function { it.pattern() }), defaultBufferLifetime)

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
            fireEvent(StageFormLoad(
                ClassPathResource(FXML__EVENT_BROWSER_STAGE),
                Supplier { primaryStage }))
        }
    }

    override fun stop() {
        weld.shutdown()
    }

}