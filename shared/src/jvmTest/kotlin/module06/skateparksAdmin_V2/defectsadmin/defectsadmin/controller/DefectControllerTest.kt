package module06.skateparksAdmin_V2.defectsadmin.defectsadmin.controller

import module06.skateparksAdmin_V2.defectsadmin.defecteditor.controller.DefectEditorAction
import module06.skateparksAdmin_V2.defectsadmin.defecteditor.controller.DefectEditorController
import module06.skateparksAdmin_V2.defectsadmin.defectexplorer.controller.DefectExplorerAction
import module06.skateparksAdmin_V2.defectsadmin.defectexplorer.controller.DefectExplorerController
import module06.skateparksAdmin_V2.defectsadmin.shared.Defect
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectRepository
import module06.xtracted.repository.urlFromResources
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNull

class DefectControllerTest {
    private lateinit var repository: DefectRepository

    @BeforeEach
    fun setup() {
        repository = DefectRepository("/data/defectDB".urlFromResources())
        repository.deleteAllDefects()
    }

    @Test
    fun testInitialization(){
        //given

        //when
        val controller = DefectController(repository)

        //then
        assertTrue(controller.state.activeController is DefectExplorerController)
    }

    @Test
    fun testSwitchToEditor(){
        //given
        val defect = repository.create(Defect(id = -1, name = "Broken ramp", parkId = 5))
        val controller = DefectController(repository)

        //when
        controller.triggerAction(DefectAdminAction.SwitchToEditor(defect.id))

        //then
        assertTrue(controller.state.activeController is DefectEditorController)
        val editor = controller.state.activeController as DefectEditorController
        assertEquals(defect, editor.state.currentFormModel.asDefect())
    }

    @Test
    fun testOnSelect(){
        //given
        val defect = repository.create(Defect(id = -1, name = "Broken ramp", parkId = 5))
        val controller = DefectController(repository)
        val explorer = controller.state.activeController as DefectExplorerController

        //when
        explorer.triggerAction(DefectExplorerAction.SelectDefect(defect.id))

        //then
        assertTrue(controller.state.activeController is DefectEditorController)
        val editor = controller.state.activeController as DefectEditorController
        assertEquals(defect.id, editor.state.currentFormModel.id.value)
    }

    @Test
    fun testSwitchToExplorer(){
        //given
        val defect = repository.create(Defect(id = -1, name = "Broken ramp", parkId = 5))
        val controller = DefectController(repository)
        controller.triggerAction(DefectAdminAction.SwitchToEditor(defect.id))

        //when
        controller.triggerAction(DefectAdminAction.SwitchToExplorer)

        //then
        assertTrue(controller.state.activeController is DefectExplorerController)
        val explorer = controller.state.activeController as DefectExplorerController
        assertTrue(explorer.state.allDefects.contains(defect))
    }

    @Test
    fun testOnCreate(){
        //given
        val controller = DefectController(repository)
        val explorer = controller.state.activeController as DefectExplorerController

        //when
        explorer.triggerAction(DefectExplorerAction.CreateDefect)

        //then
        assertTrue(controller.state.activeController is DefectEditorController)
        val editor = controller.state.activeController as DefectEditorController
        assertEquals(1, repository.countDefects())
        assertEquals(editor.state.currentFormModel.asDefect(),
                     repository.readDefect(editor.state.currentFormModel.id.value))
    }

    @Test
    fun testOnSave(){
        //given
        val defect = repository.create(Defect(id = -1, name = "Broken ramp", parkId = 5))
        val controller = DefectController(repository)
        controller.triggerAction(DefectAdminAction.SwitchToEditor(defect.id))
        val editor = controller.state.activeController as DefectEditorController
        editor.triggerAction(DefectEditorAction.SetName("Repaired ramp"))

        //when
        editor.triggerAction(DefectEditorAction.Update)

        //then
        assertTrue(controller.state.activeController is DefectExplorerController)
        val explorer = controller.state.activeController as DefectExplorerController
        assertEquals(defect.copy(name = "Repaired ramp"), explorer.state.allDefects.single())
    }

    @Test
    fun testOnBack(){
        //given
        val defect = repository.create(Defect(id = -1, name = "Broken ramp", parkId = 5))
        val controller = DefectController(repository)
        controller.triggerAction(DefectAdminAction.SwitchToEditor(defect.id))
        val editor = controller.state.activeController as DefectEditorController
        editor.triggerAction(DefectEditorAction.SetName("Unsaved name"))

        //when
        editor.triggerAction(DefectEditorAction.Back)

        //then
        assertTrue(controller.state.activeController is DefectExplorerController)
        val explorer = controller.state.activeController as DefectExplorerController
        assertEquals(defect, explorer.state.allDefects.single())
        assertEquals(defect, repository.readDefect(defect.id))
    }

    @Test
    fun testOnDelete(){
        //given
        val defect = repository.create(Defect(id = -1, name = "Broken ramp", parkId = 5))
        val controller = DefectController(repository)
        controller.triggerAction(DefectAdminAction.SwitchToEditor(defect.id))
        val editor = controller.state.activeController as DefectEditorController

        //when
        editor.triggerAction(DefectEditorAction.Delete)

        //then
        assertTrue(controller.state.activeController is DefectExplorerController)
        val explorer = controller.state.activeController as DefectExplorerController
        assertTrue(explorer.state.allDefects.isEmpty())
        assertNull(repository.readDefect(defect.id))
    }

    @Test
    fun testOnSaveBackToPark(){
        //given
        val defect = repository.create(Defect(id = -1, name = "Broken ramp", parkId = 5))
        var selectedParkId = -999
        val controller = DefectController(repository, onSkateparkSelected = { selectedParkId = it })
        controller.triggerAction(DefectAdminAction.SwitchToEditor(defect.id))
        val editor = controller.state.activeController as DefectEditorController
        editor.triggerAction(DefectEditorAction.SetName("Repaired ramp"))

        //when
        editor.triggerAction(DefectEditorAction.SaveBackToPark)

        //then
        assertEquals(5, selectedParkId)
        assertEquals(defect.copy(name = "Repaired ramp"), repository.readDefect(defect.id))
    }
}