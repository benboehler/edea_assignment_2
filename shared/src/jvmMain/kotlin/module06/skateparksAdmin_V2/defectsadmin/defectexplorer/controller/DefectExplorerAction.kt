package module06.skateparksAdmin_V2.defectsadmin.defectexplorer.controller

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Sync
import module06.xtracted.controller.Action

sealed class DefectExplorerAction(
        override val name: String,
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true) : Action {

    object ReloadAllDefects : DefectExplorerAction("Reload", Icons.Filled.Sync)
    object CreateDefect : DefectExplorerAction("Create", Icons.Filled.AddCircle)
    class SelectDefect(val defectId: Int) : DefectExplorerAction("Select defect")
}