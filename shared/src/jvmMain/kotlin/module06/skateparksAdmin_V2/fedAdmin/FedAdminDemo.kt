package module06.skateparksAdmin_V2.fedAdmin

import androidx.compose.ui.window.application
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectRepository
import module06.skateparksAdmin_V2.fedAdmin.controller.FedAdminController
import module06.skateparksAdmin_V2.fedAdmin.view.FedAdminWindow
import module06.skateparksAdmin_V2.skateparkadmin.shared.SkateparkRepository
import module06.xtracted.repository.urlFromResources

fun main(){
    val urlDefects = "/data/defectDB".urlFromResources()
    val urlParks   = "/data/skateparkDB".urlFromResources()

    val repositoryDefects = DefectRepository(urlDefects)
    val repositoryParks   = SkateparkRepository(urlParks)

    val controller = FedAdminController(repositoryParks, repositoryDefects)

    application {
        FedAdminWindow(state   = controller.state,
                       trigger = { controller.triggerAction(it) })
    }

}