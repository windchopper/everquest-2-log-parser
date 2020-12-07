package com.github.windchopper.tools.everquest.log.parser.ui

import com.github.windchopper.common.fx.cdi.form.Form
import com.github.windchopper.common.fx.cdi.form.StageFormController
import com.github.windchopper.tools.everquest.log.parser.Application
import jakarta.enterprise.context.ApplicationScoped
import javafx.scene.Parent

@ApplicationScoped
@Form(Application.FXML__PARSE_PROGRESS_STAGE) class ParseProgressStageController: StageFormController() {

    override fun afterLoad(form: Parent?, parameters: MutableMap<String, *>, formNamespace: MutableMap<String, *>) {
        super.afterLoad(form, parameters, formNamespace)
    }

}