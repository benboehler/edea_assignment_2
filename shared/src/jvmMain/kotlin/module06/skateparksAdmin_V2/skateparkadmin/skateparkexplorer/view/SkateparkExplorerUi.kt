package module06.skateparksAdmin_V2.skateparkadmin.skateparkexplorer.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.LocalContentColor
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
import module06.skateparksAdmin_V2.skateparkadmin.shared.Skatepark
import module06.skateparksAdmin_V2.skateparkadmin.skateparkexplorer.controller.SkateparkExplorerAction
import module06.skateparksAdmin_V2.skateparkadmin.skateparkexplorer.controller.SkateparkExplorerState
import module06.xtracted.view.ActionIconStrip
import module06.xtracted.view.AlignLeftRight
import module06.xtracted.view.Toolbar
import module06.xtracted.view.handCursor
import module06.xtracted.view.theme.EdeaTheme

@Composable
fun ApplicationScope.SkateparkExplorerWindow(state: SkateparkExplorerState, trigger: (SkateparkExplorerAction) -> Unit) {
    Window(title          = state.title,
           onCloseRequest = { exitApplication() },
           state          = rememberWindowState(width    = 1280.dp,
                                                height   = 700.dp,
                                                position = WindowPosition(Alignment.Center))) {
           SkateparkExplorerUi(state.allSkateparks, trigger)
    }
}

@Composable
fun SkateparkExplorerUi(skateparks: List<Skatepark>, trigger: (SkateparkExplorerAction) -> Unit) {
    EdeaTheme(darkTheme = false) {
        MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(
            surfaceContainerHighest = Color(0xFFE6E6E6)
        )) {
        CompositionLocalProvider(LocalContentColor provides Color(0xFF222222)) {

        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            Toolbar {
                AlignLeftRight {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ActionIconStrip(trigger,
                                        listOf(SkateparkExplorerAction.CreateSkatepark,
                                               SkateparkExplorerAction.ReloadAllSkateparks))
                        Headline("Skateparks Count: ${skateparks.size}")
                    }
                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(2.dp)
                    .weight(1.0f)
                    .fillMaxWidth()
            ) {

                if (skateparks.isEmpty()) {
                    NoSkateparks()
                } else {
                    SkateparkTable(skateparks, trigger)
                }
            }
        }
    }
    }
    }

}

@Composable
private fun NoSkateparks() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "No Skateparks ?",
            fontWeight = FontWeight.ExtraLight,
            fontSize = 24.sp
        )
    }
}

@Composable
private fun SkateparkTable(skateparks: List<Skatepark>, trigger: (SkateparkExplorerAction) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()
                               .padding(end = 12.dp)
                               .height(IntrinsicSize.Min)
                               .background(Color(0xFFCCCCCC))) {
            HeaderCell("Id", weight = 0.3f)
            HeaderCell("Name")
            HeaderCell("Street")
            HeaderCell("Municipality")
        }

        Box(modifier = Modifier.weight(1.0f).fillMaxWidth()) {
            val listState = rememberLazyListState()
            LazyColumn(state = listState,
                       modifier = Modifier.fillMaxSize().padding(end = 12.dp)) {
                items(items = skateparks, key = { it.id }) { skatepark ->
                    SkateparkRow(skatepark, trigger)
                }
            }
            VerticalScrollbar(adapter = rememberScrollbarAdapter(listState),
                              modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight())
        }
    }
}

@Composable
private fun SkateparkRow(skatepark: Skatepark, trigger: (SkateparkExplorerAction) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()
                           .height(IntrinsicSize.Min)
                           .handCursor()
                           .clickable { trigger(SkateparkExplorerAction.SelectSkatepark(skatepark.id)) }) {
        TableCell(weight = 0.3f) { Text(skatepark.id.toString()) }
        TableCell(align = Alignment.CenterStart) { Text(skatepark.name) }
        TableCell(align = Alignment.CenterStart) { Text(skatepark.street) }
        TableCell(align = Alignment.CenterStart) { Text(skatepark.municipality) }
    }
}


@Composable
private fun Headline(text: String) {
    Text(text       = text,
        fontSize    = 16.sp,
        fontWeight  = FontWeight.ExtraLight,
        textAlign   = TextAlign.Center,
        modifier    = Modifier.fillMaxWidth())
}

@Composable
private fun RowScope.HeaderCell(header: String, weight: Float = 1.0f) {
    TableCell(weight      = weight,
              borderColor = Color.White) {
        Text(text  = header,
             color = Color.White)
    }
}

@Composable
private fun RowScope.TableCell(weight: Float = 1.0f,
                               align: Alignment = Alignment.Center,
                               borderColor: Color = Color.LightGray,
                               content: @Composable () -> Unit) {
    Box(contentAlignment = align,
        modifier         = Modifier.border(0.5.dp, borderColor)
                                   .padding(5.dp)
                                   .weight(weight)
                                   .fillMaxHeight()) {
        content()
    }
}