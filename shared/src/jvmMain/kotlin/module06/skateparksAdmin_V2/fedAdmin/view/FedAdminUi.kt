package module06.skateparksAdmin_V2.fedAdmin.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import module06.skateparksAdmin_V2.defectsadmin.defectsadmin.controller.DefectController
import module06.skateparksAdmin_V2.fedAdmin.controller.FedAdminActions
import module06.skateparksAdmin_V2.fedAdmin.controller.FedAdminState
import module06.skateparksAdmin_V2.skateparkadmin.skateparksadmin.controller.SkateparkController
import module06.xtracted.view.theme.EdeaTheme

@Composable
fun ApplicationScope.FedAdminWindow(state: FedAdminState, trigger: (FedAdminActions) -> Unit) {
    Window(title          = state.title,
        onCloseRequest = { exitApplication() },
        state          = rememberWindowState(width    = 1280.dp,
                                             height   = 800.dp,
                                             position = WindowPosition(Alignment.Center))) {
        FedAdminUi(state, trigger)
    }
}

@Composable
fun FedAdminUi(state: FedAdminState, trigger: (FedAdminActions) -> Unit) {
    EdeaTheme {
        Scaffold(containerColor = MaterialTheme.colorScheme.surface,
                 topBar         = { FedTabs(state, trigger) }) {
            Box(modifier = Modifier.padding(it)){
                when (state.activeController) {
                    is SkateparkController -> showSkateparkUi(state.activeController)
                    is DefectController    -> showDefectUi(state.activeController)
                }
            }
        }
    }
}

@Composable
private fun FedTabs(state: FedAdminState, trigger: (FedAdminActions) -> Unit) {
    TopAppBar(backgroundColor = MaterialTheme.colorScheme.surface,
               elevation      = 0.dp,
               modifier       = Modifier.fillMaxWidth(0.42f)){
        TabRow(modifier      = Modifier.fillMaxSize(),
            selectedTabIndex = getSelectedTabIndex(state)){
            Tab(selected = getSelectedTabIndex(state) == 0,
                onClick = {  }){
                Text(text = "Skateparks")
            }
            Tab(selected  = getSelectedTabIndex(state) == 1,
                onClick   = {  }){
                Text(text = "Defects")
            }
        }
    }
}


private fun getSelectedTabIndex(state: FedAdminState): Int =
    when (state.activeController) {
        is SkateparkController -> 0
        is DefectController    -> 1
        else                   -> 0
    }


@Composable
private fun showSkateparkUi(controller: SkateparkController) {
    Text("add Skateparks Ui")
}

@Composable
private fun showDefectUi(controller: DefectController) {
    Text("add Defects Ui")
}
