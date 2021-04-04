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
import javafx.scene.Parent
import javafx.scene.control.TableView
import javafx.stage.Modality
import javafx.stage.Stage
import java.io.File

@ApplicationScoped @Form(Application.FXML__LOG_FILE_SELECTION_STAGE) class LogFileSelectionStageController: StageFormController() {

    @Inject private lateinit var stageFormLoadEvent: Event<StageFormLoad>

    @FXML private lateinit var logFileTable: TableView<LogFile>

    override fun afterLoad(form: Parent, parameters: Map<String, *>, formNamespace: Map<String, *>) {
        super.afterLoad(form, parameters, formNamespace)

        @Suppress("UNCHECKED_CAST") val files = parameters["files"] as Iterable<File>

        files.forEach { file ->
            logFileTable.items.add(LogFile(file, file.name.replace(Regex("(?i)eq2log_([^.]+).+$"), "$1")))
        }
    }

    @FXML fun parseLogFiles(event: ActionEvent) {
        stageFormLoadEvent.fire(StageFormLoad(
            ClassPathResource(Application.FXML__PARSE_PROGRESS_STAGE),
            mapOf("logFiles" to logFileTable.items)) {
                Stage().also { newStage ->
                    newStage.initOwner(stage)
                    newStage.initModality(Modality.APPLICATION_MODAL)
                    newStage.isResizable = false
                    newStage.title = Application.messages["parseProgressStage.title"]
                }
            })
    }

}