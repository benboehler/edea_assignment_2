package module06.skateparksAdmin_V2.skateparkadmin.skateparksadmin.controller

import androidx.compose.ui.graphics.vector.ImageVector
import module06.xtracted.controller.Action

sealed class SkateparkAdminAction(
        override val name: String,
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true) : Action {

    object SwitchToExplorer                : SkateparkAdminAction("Switch to Explorer")
    class SwitchToEditor(val skateparkId: Int) : SkateparkAdminAction("Switch to Editor")
}

