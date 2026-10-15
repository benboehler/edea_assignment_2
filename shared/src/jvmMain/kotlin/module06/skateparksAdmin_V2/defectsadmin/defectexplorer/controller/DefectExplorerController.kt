package module06.skateparksAdmin_V2.defectsadmin.defectexplorer.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectRepository
import module06.xtracted.controller.Controller

class DefectExplorerController(
    val parkId: Int? = null,
    val repository: DefectRepository,
    val onDefectSelected: (Int) -> Unit = {},
    val onCreate:            () -> Unit = {}) : Controller<DefectExplorerAction> {

    var state by mutableStateOf(DefectExplorerState(title      = "Defect Explorer Demo",
                                                    allDefects = repository.readAllDefects(parkId))
    )

    override fun triggerAction(action: DefectExplorerAction) {
        if(!action.enabled){
            return
        }

        when(action){

            else -> {}
        }
    }


}