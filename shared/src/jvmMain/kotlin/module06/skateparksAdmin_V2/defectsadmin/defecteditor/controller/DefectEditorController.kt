package module06.skateparksAdmin_V2.defectsadmin.defecteditor.controller

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import module06.skateparksAdmin_V2.defectsadmin.defecteditor.model.DefectFormModel
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectRepository
import module06.xtracted.controller.Controller

class DefectEditorController(id: Int,
                             val repository: DefectRepository,
                             val onFinished: () -> Unit = {},
                             val onSkateparkSelected: (Int) -> Unit = {}) : Controller<DefectEditorAction> {

    var state by mutableStateOf(DefectEditorState(title            = "Defect Editor Demo",
                                                  currentFormModel = DefectFormModel(repository.readDefect(id)!!)))

    private val dateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy")


    override fun triggerAction(action: DefectEditorAction) {
        if(!action.enabled){
            return
        }
        when(action){

            else -> {}
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