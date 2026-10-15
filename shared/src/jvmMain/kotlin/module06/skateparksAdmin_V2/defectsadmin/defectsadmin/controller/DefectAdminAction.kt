package module06.skateparksAdmin_V2.defectsadmin.defectsadmin.controller

import androidx.compose.ui.graphics.vector.ImageVector
import module06.xtracted.controller.Action

sealed class DefectAdminAction (override val name: String,
                                override val icon: ImageVector? = null,
                                override val enabled: Boolean = true) : Action {

  //to be implemented
}
