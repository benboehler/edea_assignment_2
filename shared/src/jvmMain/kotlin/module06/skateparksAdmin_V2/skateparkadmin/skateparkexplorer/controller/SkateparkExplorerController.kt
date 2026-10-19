package module06.skateparksAdmin_V2.skateparkadmin.skateparkexplorer.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import module06.skateparksAdmin_V2.skateparkadmin.shared.SkateparkRepository
import module06.xtracted.controller.Controller

class SkateparkExplorerController(
    val repository:          SkateparkRepository,
    val onSkateparkSelected: (Int) -> Unit = {},
    val onCreate:            () -> Unit = {}) : Controller<SkateparkExplorerAction> {

    var state by mutableStateOf(
        SkateparkExplorerState(title         = "Skatepark Explorer Demo",
                               allSkateparks = repository.readAllSkateparks())
    )

    override fun triggerAction(action: SkateparkExplorerAction) {
        if(!action.enabled){
            return
        }

        when(action){
            SkateparkExplorerAction.ReloadAllSkateparks ->
                state = state.copy(allSkateparks = repository.readAllSkateparks())
            else -> {}
        }
    }



}