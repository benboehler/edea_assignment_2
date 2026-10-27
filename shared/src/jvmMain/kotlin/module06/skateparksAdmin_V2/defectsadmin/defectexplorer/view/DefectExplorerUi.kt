package module06.skateparksAdmin_V2.defectsadmin.defectexplorer.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.LocalContentColor
import java.time.format.DateTimeFormatter
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
import androidx.compose.runtime.CompositionLocalProvider
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
import module06.xtracted.view.handCursor
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
    EdeaTheme(darkTheme = false) {
        MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(
            surfaceContainerHighest = Color(0xFFE6E6E6)
        )) {
        CompositionLocalProvider(LocalContentColor provides Color(0xFF222222)) {

        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            Toolbar {
                AlignLeftRight {
                    Row(verticalAlignment = Alignment.CenterVertically, ) {
                        ActionIconStrip(trigger,
                                        listOf(DefectExplorerAction.CreateDefect,
                                               DefectExplorerAction.ReloadAllDefects))
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
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()
                               .padding(end = 12.dp)
                               .height(IntrinsicSize.Min)
                               .background(Color(0xFFCCCCCC))) {
            HeaderCell("Name")
            HeaderCell("Description", weight = 2.0f)
            HeaderCell("Issue Date", weight = 0.7f)
            HeaderCell("Park Id", weight = 0.4f)
        }
        Box(modifier = Modifier.weight(1.0f).fillMaxWidth()) {
            val listState = rememberLazyListState()
            LazyColumn(state = listState,
                       modifier = Modifier.fillMaxSize().padding(end = 12.dp)) {
                items(items = defects, key = { it.id }) { defect ->
                    DefectRow(defect, trigger)
                }
            }
            VerticalScrollbar(adapter = rememberScrollbarAdapter(listState),
                              modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight())
        }
    }
}

private val dateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy")

@Composable
private fun DefectRow(defect: Defect, trigger: (DefectExplorerAction) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()
                           .height(IntrinsicSize.Min)
                           .handCursor()
                           .clickable { trigger(DefectExplorerAction.SelectDefect(defect.id)) }) {
        TableCell { Text(defect.name) }
        TableCell(weight = 2.0f) { Text(defect.description) }
        TableCell(weight = 0.7f) { Text(defect.issueDate.format(dateFormatter)) }
        TableCell(weight = 0.4f) { Text(defect.parkId.toString()) }
    }
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
              alignment = Alignment.Center,
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