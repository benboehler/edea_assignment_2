package module06.skateparksAdmin_V2.defectsadmin.defectexplorer.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import module06.skateparksAdmin_V2.defectsadmin.defectexplorer.controller.DefectExplorerAction
import module06.skateparksAdmin_V2.defectsadmin.defectexplorer.controller.DefectExplorerState
import module06.skateparksAdmin_V2.defectsadmin.shared.Defect
import module06.xtracted.view.ActionIconStrip
import module06.xtracted.view.AlignLeftRight
import module06.xtracted.view.Toolbar
import module06.xtracted.view.theme.EdeaTheme

@Composable
fun ApplicationScope.DefectExplorerWindow(state: DefectExplorerState, trigger: (DefectExplorerAction) -> Unit) {
    Window(
        title = state.title,
        onCloseRequest = { exitApplication() },
        state = rememberWindowState(width    = 1280.dp,
                                    height   = 700.dp,
                                    position = WindowPosition(Alignment.Center))) {
        DefectExplorerUi(state.allDefects, trigger)
    }
}

@Composable
fun DefectExplorerUi(defects: List<Defect>, trigger: (DefectExplorerAction) -> Unit) {
    EdeaTheme {

        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            Toolbar {
                AlignLeftRight {
                    Row(verticalAlignment = Alignment.CenterVertically, ) {
                        ActionIconStrip(trigger = trigger)
                        Headline("Defects Count: ${defects.size}")
                    }
                }
            }

            Box(contentAlignment = Alignment.Center,
                modifier         = Modifier.padding(2.dp)
                                           .weight(1.0f)
                                           .fillMaxWidth()) {
                if (defects.isEmpty()) {
                    NoDefects()
                } else {
                    DefectTable(defects, trigger)
                }
            }
        }
    }
}

@Composable
private fun NoDefects() {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text(
            text = "No defects yet",
            fontWeight = FontWeight.ExtraLight,
            fontSize = 24.sp
        )
    }
}

@Composable
private fun DefectTable(defects: List<Defect>, trigger: (DefectExplorerAction) -> Unit) {
    Text("Defects Table")
}


@Composable
private fun Headline(text: String) {
    Text(text      = text,
        fontSize   = 16.sp,
        fontWeight = FontWeight.ExtraLight,
        textAlign  = TextAlign.Center,
        modifier   = Modifier.fillMaxWidth()
    )
}

@Composable
private fun RowScope.HeaderCell(header: String, weight: Float = 1.0f) {
    TableCell(weight      = weight,
              borderColor = Color.White) {
        Text(header, color = Color.White)
    }
}

@Composable
private fun RowScope.TableCell(
    weight: Float = 1.0f,
    borderColor: Color = Color.LightGray,
    alignment: Alignment = Alignment.CenterStart,
    content: @Composable () -> Unit) {
    Box(contentAlignment = alignment,
        modifier         = Modifier.border(0.5.dp, borderColor)
                                   .padding(5.dp)
                                   .weight(weight)
                                   .fillMaxHeight()) {
        content()
    }
}