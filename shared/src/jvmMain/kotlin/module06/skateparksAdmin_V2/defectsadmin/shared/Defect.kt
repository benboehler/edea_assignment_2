package module06.skateparksAdmin_V2.defectsadmin.shared

import java.time.LocalDate

enum class DefectState(val description: String) {
    OPEN("offen"),
    PROCESSED("in Bearbeitung"),
    DONE("erledigt"),
}

fun String?.asDefectsState() : DefectState = DefectState.entries.find { it.name == this }  ?: DefectState.OPEN // enum.name is saved in DB as String


data class Defect(
    val id: Int,
    val name: String = "",
    val description: String = "",
    val status: DefectState = DefectState.OPEN,
    val issueDate: LocalDate = LocalDate.now(),
    val resolveDate: LocalDate? = null,
    val parkId: Int = -1,                           // foreign key to skatepark -1 is not assigned to any skatepark
)