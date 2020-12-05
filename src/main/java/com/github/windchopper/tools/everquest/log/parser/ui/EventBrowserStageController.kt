@file:Suppress("NestedLambdaShadowedImplicitParameter")

package com.github.windchopper.tools.everquest.log.parser.ui

import com.github.windchopper.common.fx.cdi.form.Form
import com.github.windchopper.common.fx.cdi.form.StageFormController
import com.github.windchopper.common.fx.cdi.form.StageFormLoad
import com.github.windchopper.common.util.ClassPathResource
import com.github.windchopper.tools.everquest.log.parser.Application
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Event
import jakarta.inject.Inject
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage
import java.io.File

@ApplicationScoped @Form(Application.FXML__EVENT_BROWSER_STAGE) class EventBrowserStageController: StageFormController() {

    @Inject private lateinit var stageFormLoadEvent: Event<StageFormLoad>

    @FXML fun openFile(event: ActionEvent) {
        val files = FileChooser().let {
            it.initialDirectory = Application.openFileInitialDirectory.load()?:File(System.getProperty("user.home"))
            it.showOpenMultipleDialog(stage)
        }

        files?.let {
            Application.openFileInitialDirectory.save(it.first().parentFile)
            stageFormLoadEvent.fire(StageFormLoad(
                ClassPathResource(Application.FXML__SELECT_LOG_EVENT_BUILDER_STAGE),
                mapOf("files" to it)) {
                    Stage().also { newStage ->
                        newStage.initOwner(stage)
                        newStage.initModality(Modality.APPLICATION_MODAL)
                        newStage.isResizable = false
                    }
                })
        }
    }

}