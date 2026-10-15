package module06.skateparksAdmin_V2.defectsadmin.defectsadmin.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import module06.skateparksAdmin_V2.defectsadmin.defecteditor.controller.DefectEditorController
import module06.skateparksAdmin_V2.defectsadmin.defecteditor.view.DefectEditorUi
import module06.skateparksAdmin_V2.defectsadmin.defectexplorer.controller.DefectExplorerController
import module06.skateparksAdmin_V2.defectsadmin.defectexplorer.view.DefectExplorerUi
import module06.skateparksAdmin_V2.defectsadmin.defectsadmin.controller.DefectState

@Composable
fun ApplicationScope.DefectWindow(state: DefectState) {
    Window(title          = state.title,
           onCloseRequest = { exitApplication() },
           state          = rememberWindowState(width    = 960.dp,
                                                height   = 750.dp,
                                                position = WindowPosition(Alignment.Center))) {
        DefectUi(state)
    }
}

@Composable
fun DefectUi(state: DefectState) {
    when(state.activeController){
        is DefectExplorerController -> showExplorerUi(state.activeController)
        is DefectEditorController   -> showEditorUi(state.activeController)
    }
}

@Composable
private fun showEditorUi(controller: DefectEditorController) {
    DefectEditorUi(defect  = controller.state.currentFormModel,
                   trigger = { controller.triggerAction(it) })
}

@Composable
private fun showExplorerUi(controller: DefectExplorerController) {
    DefectExplorerUi(defects = controller.state.allDefects,
                     trigger = { controller.triggerAction(it) })
}
