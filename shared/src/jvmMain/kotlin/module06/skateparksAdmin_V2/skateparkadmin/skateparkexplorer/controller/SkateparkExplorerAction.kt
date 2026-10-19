package module06.skateparksAdmin_V2.skateparkadmin.skateparkexplorer.controller

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Sync
import module06.xtracted.controller.Action

sealed class SkateparkExplorerAction(
        override val name: String,
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true) : Action {

    object ReloadAllSkateparks : SkateparkExplorerAction("Reload", Icons.Filled.Sync)
    object CreateSkatepark : SkateparkExplorerAction("Create", Icons.Filled.AddCircle)
    class SelectSkatepark(val skateparkId: Int) : SkateparkExplorerAction("Select skatepark")
}