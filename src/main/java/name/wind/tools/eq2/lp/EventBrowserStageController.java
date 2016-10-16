package name.wind.tools.eq2.lp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeTableView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import name.wind.application.cdi.fx.StageController;
import name.wind.application.cdi.fx.annotation.FXMLResource;
import name.wind.application.cdi.fx.event.FXMLResourceOpen;
import name.wind.common.util.Builder;
import name.wind.common.util.KnownSystemProperties;
import name.wind.common.util.Value;
import name.wind.tools.eq2.lp.log.LogRecord;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@ApplicationScoped @FXMLResource(FXMLResources.FXML__EVENT_BROWSER_STAGE) public class EventBrowserStageController
    extends StageController implements I18nSupport, PreferencesSupport, KnownSystemProperties {

    @Inject private Event<FXMLResourceOpen> fxmlResourceOpenEvent;
    @FXML private TreeTableView<LogRecord> eventTreeTableView;

    @FXML protected void openFile(ActionEvent event) {
        Value.of(
            Builder.direct(FileChooser::new)
                .set(target -> target::setInitialDirectory, Value.of(openFileInitialDirectoryPreferencesEntry)
                    .orElse(PROPERTY__USER_HOME.value())))
            .map(fileChooser -> fileChooser.showOpenMultipleDialog(stage))
            .ifPresent(files -> files.stream()
                .findAny().ifPresent(file -> openFileInitialDirectoryPreferencesEntry.accept(
                    file.getParentFile())))
            .ifPresent(files -> fxmlResourceOpenEvent.fire(
                new FXMLResourceOpen(
                    Builder.direct(Stage::new)
                        .set(target -> target::initOwner, stage)
                        .set(target -> target::initModality, Modality.APPLICATION_MODAL)
                        .set(target -> target::setResizable, false)
                        .get(),
                    FXMLResources.FXML__SELECT_LOG_EVENT_BUILDER_STAGE,
                    Builder.directMapBuilder((Supplier<Map<String, Object>>) HashMap::new)
                        .accept(target -> target.put("files", files))
                        .get())));
    }

}
