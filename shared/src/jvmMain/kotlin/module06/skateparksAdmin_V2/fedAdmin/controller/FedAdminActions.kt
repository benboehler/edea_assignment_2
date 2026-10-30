package module06.skateparksAdmin_V2.fedAdmin.controller

import androidx.compose.ui.graphics.vector.ImageVector
import module06.xtracted.controller.Action

sealed class FedAdminActions(override val name: String,
                             override val icon: ImageVector? = null,
                             override val enabled: Boolean = true) : Action {

    object SwitchToSkateparks : FedAdminActions("Skateparks")
    object SwitchToDefects : FedAdminActions("Defects")
}