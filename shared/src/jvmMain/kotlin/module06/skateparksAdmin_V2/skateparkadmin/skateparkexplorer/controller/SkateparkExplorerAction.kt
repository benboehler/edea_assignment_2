package module06.skateparksAdmin_V2.skateparkadmin.skateparkexplorer.controller

import androidx.compose.ui.graphics.vector.ImageVector
import module06.xtracted.controller.Action

sealed class SkateparkExplorerAction(
        override val name: String,
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true) : Action {

    object ReloadAllSkateparks : SkateparkExplorerAction("Reload")
    object CreateSkatepark : SkateparkExplorerAction("Create")
    class SelectSkatepark(val skateparkId: Int) : SkateparkExplorerAction("Select skatepark")
}