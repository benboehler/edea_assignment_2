package module06.skateparksAdmin_V2.fedAdmin.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectRepository
import module06.skateparksAdmin_V2.defectsadmin.defectsadmin.controller.DefectController
import module06.skateparksAdmin_V2.defectsadmin.defectsadmin.controller.DefectAdminAction
import module06.skateparksAdmin_V2.skateparkadmin.shared.SkateparkRepository
import module06.skateparksAdmin_V2.skateparkadmin.skateparksadmin.controller.SkateparkController
import module06.skateparksAdmin_V2.skateparkadmin.skateparksadmin.controller.SkateparkAdminAction
import module06.xtracted.controller.Controller

class FedAdminController(private val skateparkRepository: SkateparkRepository,
                         private val defectRepository: DefectRepository) : Controller<FedAdminActions> {
    var state by mutableStateOf(FedAdminState(title            = "Skatepark Federal Admin",
                                              activeController = createSkateparkController()))


   private fun createSkateparkController() : SkateparkController {
        val skateparkController = SkateparkController(
            repository        = skateparkRepository,
            onNewDefect = { triggerAction(FedAdminActions.NewDefect(it)) },
            onDefectSelected = { triggerAction(FedAdminActions.OpenDefect(it)) },
        )
        return skateparkController
    }

    override fun triggerAction(action: FedAdminActions) {
        if (!action.enabled) {
            return
        }
        when (action) {
            FedAdminActions.SwitchToSkateparks ->
                state = state.copy(activeController = createSkateparkController())
            FedAdminActions.SwitchToDefects ->
                state = state.copy(activeController = createDefectController())
            is FedAdminActions.OpenDefect -> {
                val controller = createDefectController()
                controller.triggerAction(DefectAdminAction.SwitchToEditor(action.defectId))
                state = state.copy(activeController = controller)
            }
            is FedAdminActions.NewDefect -> {
                val controller = createDefectController()
                controller.triggerAction(DefectAdminAction.CreateDefectForPark(action.parkId))
                state = state.copy(activeController = controller)
            }
            is FedAdminActions.OpenSkatepark -> {
                val controller = createSkateparkController()
                if (skateparkRepository.readSkatepark(action.parkId) != null) {
                    controller.triggerAction(SkateparkAdminAction.SwitchToEditor(action.parkId))
                }
                state = state.copy(activeController = controller)
            }
        }
    }

    private fun createDefectController() =
        DefectController(repository = defectRepository,
                         onSkateparkSelected = { triggerAction(FedAdminActions.OpenSkatepark(it)) })

}