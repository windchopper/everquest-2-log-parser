package com.github.windchopper.tools.eq2.lp;

import com.github.windchopper.common.fx.application.fx.StageController;
import com.github.windchopper.common.fx.application.fx.annotation.FXMLResource;
import com.github.windchopper.common.fx.application.fx.event.FXMLResourceOpen;
import com.github.windchopper.common.util.KnownSystemProperties;
import com.github.windchopper.common.util.Pipeliner;
import com.github.windchopper.tools.eq2.lp.log.LogRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeTableView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.File;
import java.util.HashMap;
import java.util.Optional;

@ApplicationScoped @FXMLResource(Globals.FXMLResources.FXML__EVENT_BROWSER_STAGE) public class EventBrowserStageController
    extends StageController implements KnownSystemProperties {

    @Inject private Event<FXMLResourceOpen> fxmlResourceOpenEvent;
    @FXML private TreeTableView<LogRecord> eventTreeTableView;

    @FXML protected void openFile(ActionEvent event) {
        Optional.ofNullable(Pipeliner.of(FileChooser::new)
            .set(target -> target::setInitialDirectory, Optional.ofNullable(Globals.Settings.openFileInitialDirectory.get())
                .orElse(userHomeFile.get().orElse(
                    new File(""))))
            .map(fileChooser -> fileChooser.showOpenMultipleDialog(stage))
            .get()).ifPresent(files -> {
                files.stream().findAny().map(File::getParentFile).ifPresent(Globals.Settings.openFileInitialDirectory);
                fxmlResourceOpenEvent.fire(
                    new FXMLResourceOpen(
                        Pipeliner.of(Stage::new)
                            .set(target -> target::initOwner, stage)
                            .set(target -> target::initModality, Modality.APPLICATION_MODAL)
                            .set(target -> target::setResizable, false)
                            .get(),
                        Globals.FXMLResources.FXML__SELECT_LOG_EVENT_BUILDER_STAGE,
                        Pipeliner.of(HashMap<String, Object>::new)
                            .accept(target -> target.put("files", files))
                            .get()));
            });
    }

}
