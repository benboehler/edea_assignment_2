package module06.skateparksAdmin_V2.defectsadmin.defectexplorer.controller

import androidx.compose.ui.graphics.vector.ImageVector
import module06.xtracted.controller.Action

sealed class DefectExplorerAction(
        override val name: String,
        override val icon: ImageVector? = null,
        override val enabled: Boolean) : Action {

   //to be implemented
}