package module06.skateparksAdmin_V2.defectsadmin.defectsadmin.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import module06.skateparksAdmin_V2.defectsadmin.defectexplorer.controller.DefectExplorerController
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectRepository
import module06.xtracted.controller.Controller

class DefectController(val repository: DefectRepository, val onSkateparkSelected: (Int) -> Unit = {}) : Controller<DefectAdminAction> {

    var state by mutableStateOf(DefectState(title            = "Defect Demo",
                                            activeController = createOverviewController()))

    override fun triggerAction(action: DefectAdminAction) {
        when(action){

            else -> {}
        }
    }



    private fun createOverviewController() =
        DefectExplorerController(repository       = repository,
                                 onCreate         = {  },
                                 onDefectSelected = {  })


}