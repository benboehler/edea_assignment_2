package module06.skateparksAdmin_V2.defectsadmin.defectsadmin.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import module06.skateparksAdmin_V2.defectsadmin.defectexplorer.controller.DefectExplorerController
import module06.skateparksAdmin_V2.defectsadmin.defecteditor.controller.DefectEditorController
import module06.skateparksAdmin_V2.defectsadmin.shared.Defect
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectRepository
import module06.xtracted.controller.Controller

class DefectController(val repository: DefectRepository, val onSkateparkSelected: (Int) -> Unit = {}) : Controller<DefectAdminAction> {

    var state by mutableStateOf(DefectState(title            = "Defect Demo",
                                            activeController = createOverviewController()))

    override fun triggerAction(action: DefectAdminAction) {
        when(action){
            DefectAdminAction.SwitchToExplorer -> switchToOverview()
            is DefectAdminAction.SwitchToEditor -> switchToDefect(action.defectId)
            is DefectAdminAction.CreateDefectForPark -> switchToNewDefect(action.parkId)
        }
    }



    private fun switchToDefect(defectId: Int) {
        state = state.copy(activeController = createEditorController(defectId))
    }

    private fun switchToNewDefect(parkId: Int = -1) {
        val newDefect = repository.create(Defect(id = -1, parkId = parkId))
        switchToDefect(newDefect.id)
    }

    private fun switchToOverview() {
        state = state.copy(activeController = createOverviewController())
    }

    private fun createOverviewController() =
        DefectExplorerController(repository       = repository,
                                 onCreate         = { switchToNewDefect() },
                                 onDefectSelected = { switchToDefect(it) })

    private fun createEditorController(defectId: Int) =
        DefectEditorController(id = defectId,
                               repository = repository,
                               onFinished = { switchToOverview() },
                               onSkateparkSelected = { onSkateparkSelected(it) })


}