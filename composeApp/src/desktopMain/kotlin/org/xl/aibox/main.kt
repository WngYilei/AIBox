package org.xl.aibox

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.DpSize
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "AIBox",
        state = rememberWindowState(size = DpSize(1200.dp, 900.dp))
    ) {
       App()
    }
}