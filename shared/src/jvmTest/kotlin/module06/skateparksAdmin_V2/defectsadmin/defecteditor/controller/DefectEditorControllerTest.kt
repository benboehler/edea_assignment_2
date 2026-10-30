package module06.skateparksAdmin_V2.defectsadmin.defecteditor.controller

import java.time.LocalDate
import module06.skateparksAdmin_V2.defectsadmin.shared.Defect
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectRepository
import module06.xtracted.repository.urlFromResources
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefectEditorControllerTest {
    private lateinit var repository: DefectRepository

    @BeforeEach
    fun setup() {
        repository = DefectRepository("/data/defectDB".urlFromResources())
        repository.deleteAllDefects()
    }

    @Test
    fun testInitialization(){
        //given
        val defect = repository.create(Defect(id = -1,
                                             name = "Broken ramp",
                                             description = "Loose board",
                                             issueDate = LocalDate.of(2026, 10, 30),
                                             parkId = 5))

        //when
        val controller = DefectEditorController(defect.id, repository)

        //then
        with(controller.state.currentFormModel) {
            assertEquals(defect, asDefect())
            assertEquals(defect.name, name.valueAsText)
            assertEquals("30.10.2026", issueDate.valueAsText)
            assertEquals("", resolveDate.valueAsText)
        }
    }

    @Test
    fun testSetName(){
        //given
        val defect = repository.create(Defect(id = -1, name = "Broken ramp", parkId = 5))
        val controller = DefectEditorController(defect.id, repository)
        val newName = "Damaged bench"

        //when
        controller.triggerAction(DefectEditorAction.SetName(newName))

        //then
        with(controller.state.currentFormModel.name) {
            assertEquals(newName, value)
            assertEquals(newName, valueAsText)
            assertTrue(isValid)
        }

        //when
        controller.triggerAction(DefectEditorAction.SetName(""))

        //then
        with(controller.state.currentFormModel.name) {
            assertEquals(newName, value)
            assertEquals("", valueAsText)
            assertFalse(isValid)
        }
        assertEquals(defect, repository.readDefect(defect.id))
    }
}