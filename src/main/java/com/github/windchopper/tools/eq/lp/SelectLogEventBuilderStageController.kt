package com.github.windchopper.tools.eq.lp

import com.github.windchopper.common.fx.cdi.form.Form
import com.github.windchopper.common.fx.cdi.form.StageFormController
import com.github.windchopper.tools.eq.lp.log.LogRecordPartBuilder
import jakarta.enterprise.context.ApplicationScoped
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.control.ListView
import javafx.scene.control.cell.CheckBoxListCell

@ApplicationScoped
@Form(Application.FXML__SELECT_LOG_EVENT_BUILDER_STAGE)
class SelectLogEventBuilderStageController: StageFormController() {
    private class BuilderReference(private val builder: LogRecordPartBuilder<*>) {
        private val selectedProperty: BooleanProperty = SimpleBooleanProperty(this, "selected", true)
        fun selectedProperty(): BooleanProperty {
            return selectedProperty
        }

        override fun toString(): String {
            return builder.toString()
        }

    }

    @FXML
    private val logEventBuilderListView: ListView<BuilderReference>? = null
    override fun afterLoad(form: Parent, parameters: Map<String?, *>?, formNamespace: Map<String?, *>?) {
        super.afterLoad(form, parameters, formNamespace)
        logEventBuilderListView!!.cellFactory = CheckBoxListCell.forListView { obj: BuilderReference -> obj.selectedProperty() }
    }
}