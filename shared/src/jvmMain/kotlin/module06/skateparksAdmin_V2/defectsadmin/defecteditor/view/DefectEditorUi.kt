package module06.skateparksAdmin_V2.defectsadmin.defecteditor.view

import androidx.compose.foundation.background
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import module06.skateparksAdmin_V2.defectsadmin.defecteditor.controller.DefectEditorAction
import module06.skateparksAdmin_V2.defectsadmin.defecteditor.controller.DefectEditorState
import module06.skateparksAdmin_V2.defectsadmin.defecteditor.model.DefectFormModel
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectState
import module06.xtracted.view.ActionIcon
import module06.xtracted.view.ActionIconStrip
import module06.xtracted.view.AlignLeftRight
import module06.xtracted.view.ErrorMessage
import module06.xtracted.view.FormField
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
    EdeaTheme(darkTheme = false) {
        MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(
            surfaceContainerHighest = Color(0xFFE6E6E6)
        )) {
            CompositionLocalProvider(LocalContentColor provides Color(0xFF222222)) {
                Column(modifier = Modifier.background(Color.White).fillMaxSize()) {
                    TopBar(trigger)
                    Column(modifier = Modifier.weight(1.0f).fillMaxWidth().padding(20.dp),
                           verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Box(modifier = Modifier.weight(1.0f).fillMaxWidth()) {
                            val listState = rememberLazyListState()
                            LazyColumn(state = listState,
                                       modifier = Modifier.fillMaxSize().padding(end = 20.dp)) {
                                item { Form(defect, trigger) }
                            }
                            VerticalScrollbar(adapter = rememberScrollbarAdapter(listState),
                                              modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight())
                        }
                        BottomBar(trigger, defect)
                    }
                }
            }
        }
    }
}



@Composable
private fun TopBar(trigger: (DefectEditorAction) -> Unit) {
    Toolbar {
        AlignLeftRight {
            ActionIconStrip(trigger, listOf(DefectEditorAction.Back, DefectEditorAction.Reload))
            ActionIcon(trigger = trigger, action = DefectEditorAction.Delete)
        }
    }
}

@Composable
private fun Form(defect: DefectFormModel,
                 trigger: (DefectEditorAction) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        FormField(attribute = defect.name,
                  onValueChange = { trigger(DefectEditorAction.SetName(it)) })
        if (!defect.name.isValid) {
            ErrorMessage("Please enter a name.")
        }
        FormField(attribute = defect.description,
                  onValueChange = { trigger(DefectEditorAction.SetDescription(it)) })
        FormField(attribute = defect.issueDate,
                  onValueChange = { trigger(DefectEditorAction.SetIssueDate(it)) })
        if (!defect.issueDate.isValid) {
            ErrorMessage("Enter a valid date in d.M.yyyy format.")
        }
        StatusSelection(defect, trigger)
        ResolveDateField(defect, trigger)
        FormField(attribute = defect.parkId,
                  onValueChange = { trigger(DefectEditorAction.SetParkId(it)) })
        if (!defect.parkId.isValid) {
            ErrorMessage("Enter a positive park ID or -1 for no park.")
        }
    }
}

@Composable
private fun StatusSelection(defect: DefectFormModel, trigger: (DefectEditorAction) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()) {
        Text(text = defect.status.caption,
             fontWeight = FontWeight.Light,
             fontSize = 15.sp,
             modifier = Modifier.width(100.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.weight(1.0f)) {
            DefectState.entries.forEach { status ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = defect.status.value == status,
                                onClick = { trigger(DefectEditorAction.SetStatus(status)) })
                    Text(status.description)
                }
            }
        }
    }
}

@Composable
private fun ResolveDateField(defect: DefectFormModel, trigger: (DefectEditorAction) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text(text = defect.resolveDate.caption,
                 fontWeight = FontWeight.Light,
                 fontSize = 15.sp,
                 modifier = Modifier.width(100.dp))
            OutlinedTextField(value = defect.resolveDate.valueAsText,
                              onValueChange = { trigger(DefectEditorAction.SetResolveDate(it)) },
                              enabled = defect.status.value == DefectState.DONE,
                              isError = !defect.resolveDate.isValid,
                              singleLine = true,
                              placeholder = { Text("d.M.yyyy") },
                              modifier = Modifier.weight(1.0f))
        }
        if (!defect.resolveDate.isValid) {
            ErrorMessage("Enter a valid resolution date on or after the issue date.")
        } else if (defect.status.value != DefectState.DONE) {
            Text("Select erledigt to enter a resolution date.",
                 style = MaterialTheme.typography.bodySmall,
                 modifier = Modifier.padding(start = 100.dp))
        }
    }
}

@Composable
private fun BottomBar(trigger: (DefectEditorAction) -> Unit,
                      defect: DefectFormModel) {
    AlignLeftRight {
        Button(enabled = defect.isValid && defect.parkId.value > 0,
               onClick = { trigger(DefectEditorAction.SaveBackToPark) }) {
            Text(DefectEditorAction.SaveBackToPark.name)
        }
        Button(enabled = defect.isValid,
               onClick = { trigger(DefectEditorAction.Update) }) {
            Text(DefectEditorAction.Update.name)
        }
    }
}

