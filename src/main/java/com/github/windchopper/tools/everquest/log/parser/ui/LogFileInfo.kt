@file:Suppress("UNUSED_ANONYMOUS_PARAMETER")

package com.github.windchopper.tools.everquest.log.parser.ui

import com.github.windchopper.common.fx.CellFactories
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableValue
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.util.Callback
import java.io.File

class LogFileInfo(private val file: File, private var playerName: String) {

    private val fileNameProperty = SimpleObjectProperty(file.name)
    private val playerNameProperty = SimpleStringProperty(playerName)

    companion object {

        @JvmStatic fun tableCellFactory(): Callback<TableColumn<LogFileInfo, String>, TableCell<LogFileInfo, String>> {
            return CellFactories.tableColumnCellFactory { cell, column, item, empty -> cell.setText(item) }
        }

        @JvmStatic fun tableCellValueFactory(): Callback<TableColumn.CellDataFeatures<LogFileInfo, String>, ObservableValue<String>> {
            return Callback { features ->
                when (features.tableColumn.id) {
                    "fileNameColumn" -> features.value.fileNameProperty
                    "playerNameColumn" -> features.value.playerNameProperty
                    else -> throw IllegalArgumentException("Unknown column: ${features.tableColumn.id}")
                }
            }
        }

    }

}