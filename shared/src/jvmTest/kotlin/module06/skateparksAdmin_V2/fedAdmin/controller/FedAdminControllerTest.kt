package module06.skateparksAdmin_V2.fedAdmin.controller

import module06.skateparksAdmin_V2.defectsadmin.defectsadmin.controller.DefectController
import module06.skateparksAdmin_V2.defectsadmin.defectexplorer.controller.DefectExplorerController
import module06.skateparksAdmin_V2.defectsadmin.shared.Defect
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectRepository
import module06.skateparksAdmin_V2.skateparkadmin.shared.Skatepark
import module06.skateparksAdmin_V2.skateparkadmin.shared.SkateparkRepository
import module06.skateparksAdmin_V2.skateparkadmin.skateparkexplorer.controller.SkateparkExplorerController
import module06.skateparksAdmin_V2.skateparkadmin.skateparksadmin.controller.SkateparkController
import module06.xtracted.repository.urlFromResources
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FedAdminControllerTest {
    private lateinit var skateparkRepo: SkateparkRepository
    private lateinit var defectRepo: DefectRepository

    @BeforeEach
    fun setup() {
        skateparkRepo = SkateparkRepository("/data/skateparkDB".urlFromResources())
        defectRepo = DefectRepository("/data/defectDB".urlFromResources())
        skateparkRepo.deleteAllSkateparks()
        defectRepo.deleteAllDefects()
    }

    @Test
    fun testInitialization(){
        //given

        //when
        val controller = FedAdminController(skateparkRepo, defectRepo)

        //then
        assertTrue(controller.state.activeController is SkateparkController)
        val skateparkAdmin = controller.state.activeController as SkateparkController
        assertTrue(skateparkAdmin.state.activeController is SkateparkExplorerController)
    }

    @Test
    fun testSwitchToDefects(){
        //given
        val defect = defectRepo.create(Defect(id = -1, name = "Broken ramp", parkId = 5))
        val controller = FedAdminController(skateparkRepo, defectRepo)

        //when
        controller.triggerAction(FedAdminActions.SwitchToDefects)

        //then
        assertTrue(controller.state.activeController is DefectController)
        val defectAdmin = controller.state.activeController as DefectController
        assertTrue(defectAdmin.state.activeController is DefectExplorerController)
        val explorer = defectAdmin.state.activeController as DefectExplorerController
        assertEquals(defect, explorer.state.allDefects.single())
    }

    @Test
    fun testSwitchToSkateparks(){
        //given
        val skatepark = skateparkRepo.create(Skatepark(name = "Tony Hawk Park"))
        val controller = FedAdminController(skateparkRepo, defectRepo)
        controller.triggerAction(FedAdminActions.SwitchToDefects)

        //when
        controller.triggerAction(FedAdminActions.SwitchToSkateparks)

        //then
        assertTrue(controller.state.activeController is SkateparkController)
        val skateparkAdmin = controller.state.activeController as SkateparkController
        assertTrue(skateparkAdmin.state.activeController is SkateparkExplorerController)
        val explorer = skateparkAdmin.state.activeController as SkateparkExplorerController
        assertEquals(skatepark, explorer.state.allSkateparks.single())
    }

    @Test
    fun testSwitchingTabsLoadsCurrentData(){
        //given
        val controller = FedAdminController(skateparkRepo, defectRepo)
        val skatepark = skateparkRepo.create(Skatepark(name = "Tony Hawk Park"))
        val defect = defectRepo.create(Defect(id = -1, name = "Broken ramp", parkId = skatepark.id))

        //when
        controller.triggerAction(FedAdminActions.SwitchToDefects)

        //then
        val defectAdmin = controller.state.activeController as DefectController
        val defectExplorer = defectAdmin.state.activeController as DefectExplorerController
        assertEquals(defect, defectExplorer.state.allDefects.single())

        //when
        controller.triggerAction(FedAdminActions.SwitchToSkateparks)

        //then
        val skateparkAdmin = controller.state.activeController as SkateparkController
        val skateparkExplorer = skateparkAdmin.state.activeController as SkateparkExplorerController
        assertEquals(skatepark, skateparkExplorer.state.allSkateparks.single())
    }
}