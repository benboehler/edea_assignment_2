package module06.skateparksAdmin_V2.defectsadmin.defecteditor.controller

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.RemoveCircle
import module06.xtracted.controller.Action
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectState

sealed class DefectEditorAction(override val name: String,
                                override val icon: ImageVector? = null,
                                override val enabled: Boolean = true) : Action {

    class SetName(val nameText: String) : DefectEditorAction("Set name")
    class SetDescription(val descriptionText: String) : DefectEditorAction("Set description")
    class SetIssueDate(val issueDateText: String) : DefectEditorAction("Set issue date")
    class SetParkId(val parkIdText: String) : DefectEditorAction("Set park ID")
    class SetStatus(val status: DefectState) : DefectEditorAction("Set status")
    class SetResolveDate(val resolveDateText: String) : DefectEditorAction("Set resolve date")
    object Update : DefectEditorAction("Save")
    object Back : DefectEditorAction("Back", Icons.AutoMirrored.Filled.ArrowBack)
    object Delete : DefectEditorAction("Delete", Icons.Filled.RemoveCircle)
    object SaveBackToPark : DefectEditorAction("Save and return to park")
}