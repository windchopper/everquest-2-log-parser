package com.github.windchopper.tools.everquest.log.parser.ui

import com.github.windchopper.common.fx.CellFactories
import com.github.windchopper.common.fx.cdi.form.Form
import com.github.windchopper.common.fx.cdi.form.StageFormController
import com.github.windchopper.tools.everquest.log.parser.Application
import jakarta.enterprise.context.ApplicationScoped
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.util.Callback
import java.io.File

@ApplicationScoped @Form(Application.FXML__SELECT_LOG_EVENT_BUILDER_STAGE) class LogFileInfoStageController: StageFormController() {

    @FXML private lateinit var logFileTable: TableView<LogFileInfo>

    override fun afterLoad(form: Parent, parameters: Map<String, *>, formNamespace: Map<String, *>) {
        super.afterLoad(form, parameters, formNamespace)

        @Suppress("UNCHECKED_CAST") val files = parameters["files"] as Iterable<File>

        files.forEach { file ->
            logFileTable.items.add(LogFileInfo(file, file.name.replace(Regex("(?i)eq2log_([^.]+).+$"), "$1")))
        }
    }

}