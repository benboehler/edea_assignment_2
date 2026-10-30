package module06.skateparksAdmin_V2.defectsadmin.defecteditor.controller

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import module06.skateparksAdmin_V2.defectsadmin.defecteditor.model.DefectFormModel
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectRepository
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectState
import module06.xtracted.controller.Controller

class DefectEditorController(id: Int,
                             val repository: DefectRepository,
                             val onFinished: () -> Unit = {},
                             val onSkateparkSelected: (Int) -> Unit = {}) : Controller<DefectEditorAction> {

    var state by mutableStateOf(DefectEditorState(title            = "Defect Editor Demo",
                                                  currentFormModel = DefectFormModel(repository.readDefect(id)!!)))

    private val dateFormatter = DateTimeFormatter.ofPattern("d.M.uuuu")
        .withResolverStyle(ResolverStyle.STRICT)


    override fun triggerAction(action: DefectEditorAction) {
        if(!action.enabled){
            return
        }
        when(action){
            DefectEditorAction.Delete -> {
                repository.deleteDefect(state.currentFormModel.id.value)
                onFinished()
            }
            DefectEditorAction.SaveBackToPark -> {
                val form = state.currentFormModel
                if (!form.isValid || form.parkId.value <= 0) {
                    return
                }
                repository.updateDefect(form.asDefect())
                onSkateparkSelected(form.parkId.value)
            }
            DefectEditorAction.Back -> onFinished()
            DefectEditorAction.Reload -> {
                val defect = repository.readDefect(state.currentFormModel.id.value)!!
                state = state.copy(currentFormModel = DefectFormModel(defect))
            }
            DefectEditorAction.Update -> {
                if (!state.currentFormModel.isValid) {
                    return
                }
                repository.updateDefect(state.currentFormModel.asDefect())
                onFinished()
            }
            is DefectEditorAction.SetResolveDate -> {
                val form = state.currentFormModel
                if (form.status.value != DefectState.DONE) {
                    return
                }
                val parsedDate = action.resolveDateText.toLocalDateOrNull()
                val isValid = parsedDate != null && !parsedDate.isBefore(form.issueDate.value)
                form.resolveDate = form.resolveDate.copy(
                    value = if (parsedDate != null && isValid) parsedDate else form.resolveDate.value,
                    valueAsText = action.resolveDateText,
                    isValid = isValid
                )
            }
            is DefectEditorAction.SetStatus -> {
                val form = state.currentFormModel
                form.status = form.status.copy(
                    value = action.status,
                    valueAsText = action.status.description
                )
                if (action.status == DefectState.DONE) {
                    if (form.resolveDate.valueAsText.isBlank()) {
                        val today = LocalDate.now()
                        form.resolveDate = form.resolveDate.copy(
                            value = today,
                            valueAsText = today.format(dateFormatter),
                            isValid = !today.isBefore(form.issueDate.value)
                        )
                    }
                } else {
                    form.resolveDate = form.resolveDate.copy(valueAsText = "", isValid = true)
                }
            }
            is DefectEditorAction.SetParkId -> {
                val parkId = state.currentFormModel.parkId
                val parsedId = action.parkIdText.toIntOrNull()
                val isValid = parsedId != null && (parsedId > 0 || parsedId == -1)
                state.currentFormModel.parkId = parkId.copy(
                    value = if (parsedId != null && isValid) parsedId else parkId.value,
                    valueAsText = action.parkIdText,
                    isValid = isValid
                )
            }
            is DefectEditorAction.SetIssueDate -> {
                val form = state.currentFormModel
                val issueDate = form.issueDate
                val parsedDate = action.issueDateText.toLocalDateOrNull()
                form.issueDate = issueDate.copy(
                    value = parsedDate ?: issueDate.value,
                    valueAsText = action.issueDateText,
                    isValid = parsedDate != null
                )
                if (parsedDate != null && form.status.value == DefectState.DONE) {
                    val resolveDate = form.resolveDate.valueAsText.toLocalDateOrNull()
                    val isValid = resolveDate != null && !resolveDate.isBefore(parsedDate)
                    form.resolveDate = form.resolveDate.copy(
                        value = if (resolveDate != null && isValid) resolveDate else form.resolveDate.value,
                        isValid = isValid
                    )
                }
            }
            is DefectEditorAction.SetDescription -> {
                state.currentFormModel.description = state.currentFormModel.description.copy(
                    value = action.descriptionText,
                    valueAsText = action.descriptionText
                )
            }
            is DefectEditorAction.SetName -> {
                val name = state.currentFormModel.name
                val isValid = action.nameText.isNotBlank()
                state.currentFormModel.name = name.copy(
                    value = if (isValid) action.nameText else name.value,
                    valueAsText = action.nameText,
                    isValid = isValid
                )
            }
        }
    }



    private fun String.toLocalDateOrNull(): LocalDate? {
        return try {
            LocalDate.parse(this, dateFormatter)
        } catch (_: DateTimeParseException) {
            null
        }
    }
}