package module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.view

import java.util.*
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import module06.skateparksAdmin_V2.skateparkadmin.shared.SkateparkState
import module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.controller.SkateparkEditorAction
import module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.controller.SkateparkEditorState
import module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.model.SkateparkFormModel
import module06.xtracted.view.AlignLeftRight
import module06.xtracted.view.Toolbar
import module06.xtracted.view.theme.EdeaTheme

@Composable
fun ApplicationScope.SkateparkEditorWindow(state: SkateparkEditorState, trigger: (SkateparkEditorAction) -> Unit) {
    Window(title          = state.title,
           onCloseRequest = ::exitApplication,
           state          = rememberWindowState(width    = 950.dp,
                                                height   = 850.dp,
                                                position = WindowPosition(Alignment.Center))) {
        SkateparkEditorUi(state.formModel,  state.imageBitmap, trigger)
    }
}

@Composable
fun SkateparkEditorUi(skatepark: SkateparkFormModel, imageBitmap: ImageBitmap, trigger: (SkateparkEditorAction) -> Unit ) {
    EdeaTheme {
        Column(modifier = Modifier.background(Color.White)
                                  .fillMaxSize()) {
            Bar(trigger)

            Column(verticalArrangement = Arrangement.spacedBy(5.dp),
                   modifier            = Modifier.padding(20.dp)) {
                Header(skatepark, imageBitmap)
                Box(modifier = Modifier.weight(1.0f)) {
                    val scrollState = rememberLazyListState()
                    LazyColumn(state = scrollState) {
                        item {
                            Form(skatepark, trigger)
                        }
                    }
                    VerticalScrollbar(adapter = rememberScrollbarAdapter(scrollState),
                                      modifier = Modifier.fillMaxHeight()
                                          .align(Alignment.CenterEnd))
                }
            }
        }
    }
}

@Composable
private fun Bar(trigger: (SkateparkEditorAction) -> Unit) {
    Toolbar {
        AlignLeftRight {
            Text(text = "Skatepark Editor Action Icons")
        }
    }
}

@Composable
private fun StatusSelection(skatepark: SkateparkFormModel, onValueChange: (String) -> Unit) {
    Row(verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier              = Modifier.fillMaxWidth()
                                        .padding(end = 10.dp)) {
        SkateparkState.entries.forEach {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = it == skatepark.description.value,
                            onClick = { onValueChange(it.description) })
                Text(text = it.description)
            }
        }
    }
}

@Composable
private fun Form(skatepark: SkateparkFormModel, trigger: (SkateparkEditorAction) -> Unit){
    Text("Form")
}



@Composable
private fun Header(skatepark: SkateparkFormModel, imageBitmap: ImageBitmap) {
    Text("Header")
}

val CH: Locale = Locale.of("de", "CH")

fun Number?.format(pattern: String, nullFormat: String = ""): String {
    return if (null == this) nullFormat else pattern.format(CH, this)
}

fun String?.format(nullFormat: String = "") = this ?: nullFormat