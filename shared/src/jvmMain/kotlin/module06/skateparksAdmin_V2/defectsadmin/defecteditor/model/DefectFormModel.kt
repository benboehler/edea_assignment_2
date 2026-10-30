package module06.skateparksAdmin_V2.defectsadmin.defecteditor.model

import java.time.format.DateTimeFormatter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import module06.skateparksAdmin_V2.defectsadmin.shared.Defect
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectState
import module06.xtracted.data.Attribute

val defectDateFormatter: DateTimeFormatter? = DateTimeFormatter.ofPattern("d.M.yyyy")
class DefectFormModel(defect: Defect){
    var id           by mutableStateOf(value = Attribute(caption ="Id",           value = defect.id,          valueAsText = defect.id.toString(),     isValid =true))
    var name         by mutableStateOf(value = Attribute(caption ="Name",         value = defect.name,        valueAsText = defect.name,              isValid =true))
    var description  by mutableStateOf(value = Attribute(caption ="Description",  value = defect.description, valueAsText = defect.description,       isValid =true))
    var status       by mutableStateOf(value = Attribute(caption ="Status",       value = defect.status,      valueAsText = defect.status.description, isValid =true))
    var issueDate    by mutableStateOf(value = Attribute(caption ="Issue Date", value = defect.issueDate, valueAsText = defect.issueDate.format(defectDateFormatter), isValid = true))
    var resolveDate  by mutableStateOf(value = Attribute(
        caption = "Resolve Date",
        value = defect.resolveDate ?: defect.issueDate, // avoid null for the value as Attribute has a non-null value!
        valueAsText = defect.resolveDate?.format(defectDateFormatter) ?: "",
        isValid = true)
    )
    var parkId       by mutableStateOf(Attribute("Park Id",   defect.parkId,  defect.parkId.toString(), true))

    val isValid: Boolean
        get() = listOf(id, name, description, status, issueDate, resolveDate, parkId).all { it.isValid } &&
                name.valueAsText.isNotBlank() &&
                (parkId.value > 0 || parkId.value == -1) &&
                (status.value != DefectState.DONE ||
                 (resolveDate.valueAsText.isNotBlank() && !resolveDate.value.isBefore(issueDate.value)))

    fun asDefect() =
        Defect(id.value,
               name.value,
               description.value,
               status.value,
               issueDate.value,
               if (resolveDate.valueAsText.isBlank()) null else resolveDate.value,
               parkId.value)

}
