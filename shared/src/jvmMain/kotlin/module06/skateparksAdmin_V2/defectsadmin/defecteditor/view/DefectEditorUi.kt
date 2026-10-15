package module06.skateparksAdmin_V2.defectsadmin.defecteditor.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import module06.skateparksAdmin_V2.defectsadmin.defecteditor.controller.DefectEditorAction
import module06.skateparksAdmin_V2.defectsadmin.defecteditor.controller.DefectEditorState
import module06.skateparksAdmin_V2.defectsadmin.defecteditor.model.DefectFormModel
import module06.xtracted.view.Toolbar
import module06.xtracted.view.theme.EdeaTheme

@Composable
fun ApplicationScope.DefectEditorWindow(state: DefectEditorState, trigger: (DefectEditorAction) -> Unit) {
    Window(title          = state.title,
           onCloseRequest = ::exitApplication,
           state          = rememberWindowState(width    = 900.dp,
                                                height   = 700.dp,
                                                position = WindowPosition(Alignment.Center))) {
        DefectEditorUi(state.currentFormModel, trigger)
    }
}

@Composable
fun DefectEditorUi(defect: DefectFormModel, trigger: (DefectEditorAction) -> Unit) {
    EdeaTheme {
        Column(modifier = Modifier.background(Color.White)) {
            TopBar(trigger)
            Form(defect, trigger)
            Spacer(Modifier.weight(1.0f))
            BottomBar(trigger, defect)
        }
    }
}



@Composable
private fun TopBar(trigger: (DefectEditorAction) -> Unit) {
    Toolbar {
        Text("Defects Editor Action Icons")
    }
}

@Composable
private fun Form(defect: DefectFormModel,
                 trigger: (DefectEditorAction) -> Unit) {
    Text("Defects Form")
}

@Composable
private fun BottomBar(trigger: (DefectEditorAction) -> Unit,
                      defect: DefectFormModel) {
    Text("Defects Form Action Icons")
}

