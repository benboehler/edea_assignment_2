package module06.skateparksAdmin_V2.skateparkadmin.skateparksadmin.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import module06.skateparksAdmin_V2.skateparkadmin.shared.Skatepark
import module06.skateparksAdmin_V2.skateparkadmin.shared.SkateparkRepository
import module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.controller.SkateparkEditorController
import module06.skateparksAdmin_V2.skateparkadmin.skateparkexplorer.controller.SkateparkExplorerController
import module06.xtracted.controller.Controller

class SkateparkController(val repository: SkateparkRepository,
                          val onNewDefect: (Int) -> Unit = {},
                          val onDefectSelected: (Int) -> Unit = {}) : Controller<SkateparkAdminAction> {

    var state by mutableStateOf(SkateparkState(title            = "Skatepark Demo",
                                               activeController = createExplorerController()))

    override fun triggerAction(action: SkateparkAdminAction) {
        when(action){
            is SkateparkAdminAction.SwitchToEditor    -> switchToSkatepark(action.skateparkId)
            is SkateparkAdminAction.SwitchToExplorer  -> switchToOverview()
        }
    }

    private fun switchToSkatepark(skateparkId: Int) {
        state = state.copy(activeController = createEditorController(skateparkId))
    }

    private fun switchToNewSkatepark() {
        val newSkatepark = repository.create(Skatepark(id=-999))
        switchToSkatepark(newSkatepark.id)
    }

    private fun switchToOverview() {
        state = state.copy(activeController = createExplorerController())
    }

    private fun createExplorerController() =
        SkateparkExplorerController(repository          = repository,
                                    onCreate            = { switchToNewSkatepark() },
                                    onSkateparkSelected = { switchToSkatepark(it) },
        )

    private fun createEditorController(skateparkId: Int) =
        SkateparkEditorController(id               = skateparkId,
                                  repository       = repository,
                                  onFinished       = { switchToOverview() },
                                  onNewDefect      = { onNewDefect(skateparkId)},
                                  onDefectSelected = { onDefectSelected(it) } )
}