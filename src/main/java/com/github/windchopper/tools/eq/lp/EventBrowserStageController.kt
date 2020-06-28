package com.github.windchopper.tools.eq.lp

import com.github.windchopper.common.fx.cdi.form.Form
import com.github.windchopper.common.fx.cdi.form.StageFormController
import com.github.windchopper.common.fx.cdi.form.StageFormLoad
import com.github.windchopper.common.util.ClassPathResource
import com.github.windchopper.common.util.Pipeliner
import com.github.windchopper.tools.eq.lp.log.LogRecord
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Event
import jakarta.inject.Inject
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.TreeTableView
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage
import java.io.File
import java.util.*
import java.util.function.Consumer

@ApplicationScoped
@Form(Application.FXML__EVENT_BROWSER_STAGE)
class EventBrowserStageController: StageFormController() {
    @Inject
    private val fxmlResourceOpenEvent: Event<StageFormLoad>? = null

    @FXML
    private val eventTreeTableView: TreeTableView<LogRecord>? = null

    @FXML
    fun openFile(event: ActionEvent?) {
        val fileChooser = Pipeliner.of { FileChooser() }
            .set({ target: FileChooser -> Consumer { file: File? -> target.initialDirectory = file } }, Optional.ofNullable(Application.openFileInitialDirectory.load())
                .orElseGet {
                    val initialDirectory = File(System.getProperty("user.home"))
                    Application.openFileInitialDirectory.save(initialDirectory)
                    initialDirectory
                })
            .get()
        Optional.ofNullable(fileChooser.showOpenMultipleDialog(stage))
            .ifPresent { files: List<File>? ->
                fxmlResourceOpenEvent!!.fire(
                    StageFormLoad(
                        ClassPathResource(Application.FXML__SELECT_LOG_EVENT_BUILDER_STAGE),
                        mapOf("files" to files),
                        Pipeliner.of { Stage() }
                            .set({ target: Stage -> Consumer { window: Stage? -> target.initOwner(window) } }, stage)
                            .set({ target: Stage -> Consumer { modality: Modality? -> target.initModality(modality) } }, Modality.APPLICATION_MODAL)
                            .set({ target: Stage -> Consumer { b: Boolean? -> target.isResizable = b!! } }, false)))
            }
    }
}