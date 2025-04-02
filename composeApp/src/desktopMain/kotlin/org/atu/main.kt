package org.atu

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "csd_project_l00188362",
    ) {
        App()
    }
}