package module06.skateparksAdmin_V2.skateparkadmin.skateparksadmin.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.controller.SkateparkEditorController
import module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.view.SkateparkEditorUi
import module06.skateparksAdmin_V2.skateparkadmin.skateparkexplorer.controller.SkateparkExplorerController
import module06.skateparksAdmin_V2.skateparkadmin.skateparkexplorer.view.SkateparkExplorerUi
import module06.skateparksAdmin_V2.skateparkadmin.skateparksadmin.controller.SkateparkState

@Composable
fun ApplicationScope.SkateparkWindow(state: SkateparkState) {
    Window(title          = state.title,
           onCloseRequest = { exitApplication() },
           state          = rememberWindowState(width    = 960.dp,
                                                height   = 750.dp,
                                                position = WindowPosition(Alignment.Center))) {
        SkateparkUi(state)
    }
}

@Composable
fun SkateparkUi(state: SkateparkState) {
    when (state.activeController) {
        is SkateparkExplorerController -> showExplorerUi(state.activeController)
        is SkateparkEditorController   -> showEditorUi(state.activeController)
    }
}

@Composable
private fun showEditorUi(controller: SkateparkEditorController) {
    SkateparkEditorUi(skatepark     = controller.state.formModel,
                      imageBitmap   = controller.state.imageBitmap,
                      trigger       = { controller.triggerAction(it) },
                    )
}

@Composable
private fun showExplorerUi(controller: SkateparkExplorerController) {
    SkateparkExplorerUi(skateparks = controller.state.allSkateparks,
                        trigger    = { controller.triggerAction(it) })
}
