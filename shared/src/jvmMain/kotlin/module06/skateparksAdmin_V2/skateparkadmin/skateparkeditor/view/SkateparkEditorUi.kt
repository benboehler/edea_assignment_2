package module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.view

import java.util.*
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Button
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
import module06.xtracted.view.ActionIcon
import module06.xtracted.view.ActionIconStrip
import module06.xtracted.view.FormField
import module06.xtracted.view.ReadOnlyFormField
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
    EdeaTheme(darkTheme = false) {
        Column(modifier = Modifier.background(Color.White)
                                  .fillMaxSize()) {
            Bar(trigger)

            Column(verticalArrangement = Arrangement.spacedBy(16.dp),
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
                SaveBar(skatepark, trigger)
            }
        }
    }
}

@Composable
private fun Bar(trigger: (SkateparkEditorAction) -> Unit) {
    Toolbar {
        AlignLeftRight {
            ActionIconStrip(trigger,
                            listOf(SkateparkEditorAction.Back, SkateparkEditorAction.Reload))
            ActionIcon(trigger = trigger, action = SkateparkEditorAction.Delete)
        }
    }
}

@Composable
private fun SaveBar(skatepark: SkateparkFormModel, trigger: (SkateparkEditorAction) -> Unit) {
    Row(horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()) {
        Button(enabled = skatepark.isValid,
               onClick = { trigger(SkateparkEditorAction.Update) }) {
            Text(SkateparkEditorAction.Update.name)
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
    Column(verticalArrangement = Arrangement.spacedBy(12.dp),
           modifier = Modifier.fillMaxWidth().padding(end = 20.dp)) {
        FormField(attribute = skatepark.name,
                  onValueChange = { trigger(SkateparkEditorAction.SetName(it)) })
        Text(skatepark.description.caption)
        StatusSelection(skatepark,
                        onValueChange = { trigger(SkateparkEditorAction.SetDescription(it)) })
        FormField(attribute = skatepark.street,
                  onValueChange = { trigger(SkateparkEditorAction.SetStreet(it)) })
        FormField(attribute = skatepark.municipality,
                  onValueChange = { trigger(SkateparkEditorAction.SetMunicipality(it)) })
        FormField(attribute = skatepark.website,
                  onValueChange = { trigger(SkateparkEditorAction.SetWebsite(it)) })
        FormField(attribute = skatepark.imageUrl,
                  onValueChange = { trigger(SkateparkEditorAction.SetImageUrl(it)) })
    }
}



@Composable
private fun Header(skatepark: SkateparkFormModel, imageBitmap: ImageBitmap) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp),
           modifier = Modifier.fillMaxWidth()
                              .background(Color(0xFFF2F2F2))
                              .padding(12.dp)) {
        ReadOnlyFormField(skatepark.name)
        ReadOnlyFormField(skatepark.street)
        ReadOnlyFormField(skatepark.municipality)
    }
}

val CH: Locale = Locale.of("de", "CH")

fun Number?.format(pattern: String, nullFormat: String = ""): String {
    return if (null == this) nullFormat else pattern.format(CH, this)
}

fun String?.format(nullFormat: String = "") = this ?: nullFormat