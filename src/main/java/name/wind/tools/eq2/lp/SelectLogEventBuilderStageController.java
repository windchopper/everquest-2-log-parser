package name.wind.tools.eq2.lp;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import name.wind.application.cdi.fx.StageController;
import name.wind.application.cdi.fx.annotation.FXMLResource;
import name.wind.tools.eq2.lp.log.LogRecordPartBuilder;
import name.wind.tools.eq2.lp.log.PartBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.Map;

@ApplicationScoped @FXMLResource(FXMLResources.FXML__SELECT_LOG_EVENT_BUILDER_STAGE) public class SelectLogEventBuilderStageController
    extends StageController implements I18nSupport, PreferencesSupport {

    private static class BuilderReference {

        private final LogRecordPartBuilder<?> builder;
        private final BooleanProperty selectedProperty = new SimpleBooleanProperty(this, "selected", true);

        public BuilderReference(LogRecordPartBuilder<?> builder) {
            this.builder = builder;
        }

        public BooleanProperty selectedProperty() {
            return selectedProperty;
        }

        @Override public String toString() {
            return builder.toString();
        }

    }

    @Inject @PartBuilder Instance<LogRecordPartBuilder<?>> logEventBuilders;
    @FXML private ListView<BuilderReference> logEventBuilderListView;

    @Override protected void start(Stage stage, String fxmlResource, Map<String, ?> parameters) {
        super.start(stage, fxmlResource, parameters);
        logEventBuilderListView.setCellFactory(CheckBoxListCell.forListView(BuilderReference::selectedProperty));
        logEventBuilders.forEach(builder -> logEventBuilderListView.getItems().add(
            new BuilderReference(builder)));
    }

}
