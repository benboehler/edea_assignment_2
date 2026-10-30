package module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.view

import java.util.*
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import module06.skateparksAdmin_V2.skateparkadmin.shared.SkateparkState
import module06.skateparksAdmin_V2.defectsadmin.shared.Defect
import module06.skateparksAdmin_V2.defectsadmin.defectexplorer.controller.DefectExplorerAction
import module06.skateparksAdmin_V2.defectsadmin.defectexplorer.view.DefectExplorerUi
import module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.controller.SkateparkEditorAction
import module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.controller.SkateparkEditorState
import module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.model.SkateparkFormModel
import module06.xtracted.view.AlignLeftRight
import module06.xtracted.view.ActionIcon
import module06.xtracted.view.ActionIconStrip
import module06.xtracted.view.FormField
import module06.xtracted.view.Toolbar
import module06.xtracted.view.theme.EdeaTheme

@Composable
fun ApplicationScope.SkateparkEditorWindow(state: SkateparkEditorState, trigger: (SkateparkEditorAction) -> Unit) {
    Window(title          = state.title,
           onCloseRequest = ::exitApplication,
           state          = rememberWindowState(width    = 950.dp,
                                                height   = 850.dp,
                                                position = WindowPosition(Alignment.Center))) {
        SkateparkEditorUi(skatepark = state.formModel,
                          imageBitmap = state.imageBitmap,
                          trigger = trigger,
                          parkDefects = state.parkDefects,
                          triggerDefect = { state.defectExplorerController.triggerAction(it) })
    }
}

@Composable
fun SkateparkEditorUi(skatepark: SkateparkFormModel,
                     imageBitmap: ImageBitmap,
                     trigger: (SkateparkEditorAction) -> Unit,
                     parkDefects: List<Defect>,
                     triggerDefect: (DefectExplorerAction) -> Unit) {
    EdeaTheme(darkTheme = false) {
        Column(modifier = Modifier.background(Color.White)
                                  .fillMaxSize()) {
            Bar(trigger)

            Box(modifier = Modifier.weight(1.0f).fillMaxWidth()) {
                val scrollState = rememberLazyListState()
                LazyColumn(state = scrollState,
                           contentPadding = PaddingValues(20.dp),
                           verticalArrangement = Arrangement.spacedBy(24.dp),
                           modifier = Modifier.fillMaxSize().padding(end = 12.dp)) {
                    item {
                        Header(skatepark, imageBitmap)
                    }
                    item {
                        Form(skatepark, trigger)
                    }
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(240.dp)) {
                            DefectExplorerUi(parkDefects, triggerDefect)
                        }
                    }
                    item {
                        SaveBar(skatepark, trigger)
                    }
                }
                VerticalScrollbar(adapter = rememberScrollbarAdapter(scrollState),
                                  modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd))
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
    Column(verticalArrangement = Arrangement.spacedBy(16.dp),
           modifier = Modifier.fillMaxWidth()) {
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
    Box(modifier = Modifier.fillMaxWidth()
                           .height(240.dp)
                           .clip(RoundedCornerShape(12.dp))) {
        Image(bitmap = imageBitmap,
              contentDescription = "Photo of ${skatepark.name.value}",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize())
        Box(modifier = Modifier.matchParentSize().background(
            Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)))
        ))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp),
               modifier = Modifier.align(Alignment.BottomStart).padding(20.dp)) {
            Text(text = skatepark.name.value,
                 color = Color.White,
                 fontWeight = FontWeight.Light,
                 fontSize = 32.sp)
            Text(text = listOf(skatepark.street.value, skatepark.municipality.value)
                            .filter { it.isNotBlank() }.joinToString(", "),
                 color = Color.White,
                 fontSize = 20.sp)
        }
    }
}

val CH: Locale = Locale.of("de", "CH")

fun Number?.format(pattern: String, nullFormat: String = ""): String {
    return if (null == this) nullFormat else pattern.format(CH, this)
}

fun String?.format(nullFormat: String = "") = this ?: nullFormat
