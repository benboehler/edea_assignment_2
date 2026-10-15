package module06.skateparksAdmin_V2.fedAdmin.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectRepository
import module06.skateparksAdmin_V2.skateparkadmin.shared.SkateparkRepository
import module06.skateparksAdmin_V2.skateparkadmin.skateparksadmin.controller.SkateparkController
import module06.xtracted.controller.Controller

class FedAdminController(private val skateparkRepository: SkateparkRepository,
                         private val defectRepository: DefectRepository) : Controller<FedAdminActions> {
    var state by mutableStateOf(FedAdminState(title            = "Skatepark Federal Admin",
                                              activeController = createSkateparkController()))


   private fun createSkateparkController(skateparkId: Int? = null) : SkateparkController {
        val skateparkController = SkateparkController(
            repository        = skateparkRepository,
        )
        return skateparkController
    }

    override fun triggerAction(action: FedAdminActions) {
        //to be implemented
    }

}