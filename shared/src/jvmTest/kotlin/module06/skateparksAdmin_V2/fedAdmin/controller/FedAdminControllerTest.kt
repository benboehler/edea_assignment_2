package module06.skateparksAdmin_V2.fedAdmin.controller

import module06.skateparksAdmin_V2.defectsadmin.defectsadmin.controller.DefectController
import module06.skateparksAdmin_V2.defectsadmin.defectexplorer.controller.DefectExplorerController
import module06.skateparksAdmin_V2.defectsadmin.defectexplorer.controller.DefectExplorerAction
import module06.skateparksAdmin_V2.defectsadmin.defecteditor.controller.DefectEditorAction
import module06.skateparksAdmin_V2.defectsadmin.defecteditor.controller.DefectEditorController
import module06.skateparksAdmin_V2.defectsadmin.shared.Defect
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectRepository
import module06.skateparksAdmin_V2.skateparkadmin.shared.Skatepark
import module06.skateparksAdmin_V2.skateparkadmin.shared.SkateparkRepository
import module06.skateparksAdmin_V2.skateparkadmin.skateparkexplorer.controller.SkateparkExplorerController
import module06.skateparksAdmin_V2.skateparkadmin.skateparkexplorer.controller.SkateparkExplorerAction
import module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.controller.SkateparkEditorController
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
    @Test
    fun testParkDefectExplorerFiltersAndReloads(){
        //given
        val park = skateparkRepo.create(Skatepark(name = "Tony Hawk Park"))
        val otherPark = skateparkRepo.create(Skatepark(name = "Other park"))
        val defect = defectRepo.create(Defect(id = -1, name = "Broken ramp", parkId = park.id))
        defectRepo.create(Defect(id = -1, name = "Broken bench", parkId = otherPark.id))
        val controller = FedAdminController(skateparkRepo, defectRepo)

        //when
        controller.triggerAction(FedAdminActions.OpenSkatepark(park.id))

        //then
        val skateparkAdmin = controller.state.activeController as SkateparkController
        val editor = skateparkAdmin.state.activeController as SkateparkEditorController
        assertEquals(defect, editor.state.parkDefects.single())

        //given
        val newDefect = defectRepo.create(Defect(id = -1, name = "Loose board", parkId = park.id))

        //when
        editor.state.defectExplorerController.triggerAction(DefectExplorerAction.ReloadAllDefects)

        //then
        assertEquals(2, editor.state.parkDefects.size)
        assertTrue(editor.state.parkDefects.contains(defect))
        assertTrue(editor.state.parkDefects.contains(newDefect))
    }

    @Test
    fun testOpenDefectFromParkAndSaveBack(){
        //given
        val park = skateparkRepo.create(Skatepark(name = "Tony Hawk Park"))
        val defect = defectRepo.create(Defect(id = -1, name = "Broken ramp", parkId = park.id))
        val controller = FedAdminController(skateparkRepo, defectRepo)
        val skateparkAdmin = controller.state.activeController as SkateparkController
        val explorer = skateparkAdmin.state.activeController as SkateparkExplorerController
        explorer.triggerAction(SkateparkExplorerAction.SelectSkatepark(park.id))
        val parkEditor = skateparkAdmin.state.activeController as SkateparkEditorController

        //when
        parkEditor.state.defectExplorerController.triggerAction(DefectExplorerAction.SelectDefect(defect.id))

        //then
        assertTrue(controller.state.activeController is DefectController)
        val defectAdmin = controller.state.activeController as DefectController
        val defectEditor = defectAdmin.state.activeController as DefectEditorController
        assertEquals(defect.id, defectEditor.state.currentFormModel.id.value)

        //given
        defectEditor.triggerAction(DefectEditorAction.SetName("Repaired ramp"))

        //when
        defectEditor.triggerAction(DefectEditorAction.SaveBackToPark)

        //then
        assertTrue(controller.state.activeController is SkateparkController)
        val returnedAdmin = controller.state.activeController as SkateparkController
        val returnedEditor = returnedAdmin.state.activeController as SkateparkEditorController
        assertEquals(park.id, returnedEditor.state.formModel.id.value)
        assertEquals(defect.copy(name = "Repaired ramp"), returnedEditor.state.parkDefects.single())
    }

    @Test
    fun testCreateDefectFromParkAndSaveBack(){
        //given
        val park = skateparkRepo.create(Skatepark(name = "Tony Hawk Park"))
        val controller = FedAdminController(skateparkRepo, defectRepo)
        controller.triggerAction(FedAdminActions.OpenSkatepark(park.id))
        val skateparkAdmin = controller.state.activeController as SkateparkController
        val parkEditor = skateparkAdmin.state.activeController as SkateparkEditorController

        //when
        parkEditor.state.defectExplorerController.triggerAction(DefectExplorerAction.CreateDefect)

        //then
        assertTrue(controller.state.activeController is DefectController)
        val defectAdmin = controller.state.activeController as DefectController
        val defectEditor = defectAdmin.state.activeController as DefectEditorController
        val newDefectId = defectEditor.state.currentFormModel.id.value
        assertEquals(park.id, defectEditor.state.currentFormModel.parkId.value)
        assertEquals(park.id, defectRepo.readDefect(newDefectId)?.parkId)
        assertEquals(1, defectRepo.countDefects())

        //given
        defectEditor.triggerAction(DefectEditorAction.SetName("Broken ramp"))

        //when
        defectEditor.triggerAction(DefectEditorAction.SaveBackToPark)

        //then
        val returnedAdmin = controller.state.activeController as SkateparkController
        val returnedEditor = returnedAdmin.state.activeController as SkateparkEditorController
        assertEquals(park.id, returnedEditor.state.formModel.id.value)
        assertEquals(newDefectId, returnedEditor.state.parkDefects.single().id)
        assertEquals("Broken ramp", returnedEditor.state.parkDefects.single().name)
        assertEquals(1, defectRepo.countDefects())
    }

    @Test
    fun testNormalDefectSaveReturnsToDefectExplorer(){
        //given
        val park = skateparkRepo.create(Skatepark(name = "Tony Hawk Park"))
        val defect = defectRepo.create(Defect(id = -1, name = "Broken ramp", parkId = park.id))
        val controller = FedAdminController(skateparkRepo, defectRepo)
        controller.triggerAction(FedAdminActions.OpenSkatepark(park.id))
        val skateparkAdmin = controller.state.activeController as SkateparkController
        val parkEditor = skateparkAdmin.state.activeController as SkateparkEditorController
        parkEditor.state.defectExplorerController.triggerAction(DefectExplorerAction.SelectDefect(defect.id))
        val defectAdmin = controller.state.activeController as DefectController
        val defectEditor = defectAdmin.state.activeController as DefectEditorController
        defectEditor.triggerAction(DefectEditorAction.SetName("Repaired ramp"))

        //when
        defectEditor.triggerAction(DefectEditorAction.Update)

        //then
        assertTrue(controller.state.activeController is DefectController)
        assertTrue(defectAdmin.state.activeController is DefectExplorerController)
        val explorer = defectAdmin.state.activeController as DefectExplorerController
        assertEquals(defect.copy(name = "Repaired ramp"), explorer.state.allDefects.single())
    }
}