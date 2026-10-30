package module06.skateparksAdmin_V2.defectsadmin.defecteditor.controller

import java.time.LocalDate
import module06.skateparksAdmin_V2.defectsadmin.shared.Defect
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectRepository
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectState
import module06.xtracted.repository.urlFromResources
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertNull

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

    @Test
    fun testSetDescription(){
        //given
        val defect = repository.create(Defect(id = -1, name = "Broken ramp",
                                             description = "Loose board", parkId = 5))
        val controller = DefectEditorController(defect.id, repository)
        val newDescription = "Two boards need replacing"

        //when
        controller.triggerAction(DefectEditorAction.SetDescription(newDescription))

        //then
        with(controller.state.currentFormModel.description) {
            assertEquals(newDescription, value)
            assertEquals(newDescription, valueAsText)
            assertTrue(isValid)
        }

        //when
        controller.triggerAction(DefectEditorAction.SetDescription(""))

        //then
        with(controller.state.currentFormModel.description) {
            assertEquals("", value)
            assertEquals("", valueAsText)
            assertTrue(isValid)
        }
        assertEquals(defect, repository.readDefect(defect.id))
    }

    @Test
    fun testSetIssueDate(){
        //given
        val defect = repository.create(Defect(id = -1, name = "Broken ramp",
                                             issueDate = LocalDate.of(2026, 10, 30), parkId = 5))
        val controller = DefectEditorController(defect.id, repository)
        val newIssueDate = LocalDate.of(2026, 11, 1)

        //when
        controller.triggerAction(DefectEditorAction.SetIssueDate("1.11.2026"))

        //then
        with(controller.state.currentFormModel.issueDate) {
            assertEquals(newIssueDate, value)
            assertEquals("1.11.2026", valueAsText)
            assertTrue(isValid)
        }

        listOf("", "x", "31.2.2026").forEach { invalidDate ->
            //when
            controller.triggerAction(DefectEditorAction.SetIssueDate(invalidDate))

            //then
            with(controller.state.currentFormModel.issueDate) {
                assertEquals(newIssueDate, value)
                assertEquals(invalidDate, valueAsText)
                assertFalse(isValid)
            }
        }

        //when
        controller.triggerAction(DefectEditorAction.SetIssueDate("2.11.2026"))

        //then
        with(controller.state.currentFormModel.issueDate) {
            assertEquals(LocalDate.of(2026, 11, 2), value)
            assertEquals("2.11.2026", valueAsText)
            assertTrue(isValid)
        }
        assertEquals(defect, repository.readDefect(defect.id))
    }

    @Test
    fun testSetParkId(){
        //given
        val defect = repository.create(Defect(id = -1, name = "Broken ramp", parkId = 5))
        val controller = DefectEditorController(defect.id, repository)

        //when
        controller.triggerAction(DefectEditorAction.SetParkId("7"))

        //then
        with(controller.state.currentFormModel.parkId) {
            assertEquals(7, value)
            assertEquals("7", valueAsText)
            assertTrue(isValid)
        }

        listOf("", "x", "0", "-2", "1.5").forEach { invalidId ->
            //when
            controller.triggerAction(DefectEditorAction.SetParkId(invalidId))

            //then
            with(controller.state.currentFormModel.parkId) {
                assertEquals(7, value)
                assertEquals(invalidId, valueAsText)
                assertFalse(isValid)
            }
        }

        //when
        controller.triggerAction(DefectEditorAction.SetParkId("-1"))

        //then
        with(controller.state.currentFormModel.parkId) {
            assertEquals(-1, value)
            assertEquals("-1", valueAsText)
            assertTrue(isValid)
        }
        assertEquals(defect, repository.readDefect(defect.id))
    }

    @Test
    fun testSetStatus(){
        //given
        val today = LocalDate.now()
        val defect = repository.create(Defect(id = -1, name = "Broken ramp",
                                             issueDate = today.minusDays(2), parkId = 5))
        val controller = DefectEditorController(defect.id, repository)

        //when
        controller.triggerAction(DefectEditorAction.SetStatus(DefectState.DONE))

        //then
        with(controller.state.currentFormModel) {
            assertEquals(DefectState.DONE, status.value)
            assertEquals(DefectState.DONE.description, status.valueAsText)
            assertEquals(today, resolveDate.value)
            assertEquals(today, asDefect().resolveDate)
            assertTrue(resolveDate.isValid)
        }

        listOf(DefectState.PROCESSED, DefectState.OPEN).forEach { newStatus ->
            //given
            controller.triggerAction(DefectEditorAction.SetStatus(DefectState.DONE))

            //when
            controller.triggerAction(DefectEditorAction.SetStatus(newStatus))

            //then
            with(controller.state.currentFormModel) {
                assertEquals(newStatus, status.value)
                assertEquals(newStatus.description, status.valueAsText)
                assertEquals("", resolveDate.valueAsText)
                assertNull(asDefect().resolveDate)
                assertTrue(resolveDate.isValid)
            }
        }
        assertEquals(defect, repository.readDefect(defect.id))
    }

    @Test
    fun testSetResolveDate(){
        //given
        val defect = repository.create(Defect(id = -1, name = "Broken ramp",
                                             status = DefectState.DONE,
                                             issueDate = LocalDate.of(2026, 10, 20),
                                             resolveDate = LocalDate.of(2026, 10, 22), parkId = 5))
        val controller = DefectEditorController(defect.id, repository)

        //when
        controller.triggerAction(DefectEditorAction.SetResolveDate("23.10.2026"))

        //then
        with(controller.state.currentFormModel.resolveDate) {
            assertEquals(LocalDate.of(2026, 10, 23), value)
            assertEquals("23.10.2026", valueAsText)
            assertTrue(isValid)
        }

        //when
        controller.triggerAction(DefectEditorAction.SetResolveDate("20.10.2026"))

        //then
        with(controller.state.currentFormModel.resolveDate) {
            assertEquals(defect.issueDate, value)
            assertTrue(isValid)
        }

        listOf("", "x", "31.2.2026", "19.10.2026").forEach { invalidDate ->
            //when
            controller.triggerAction(DefectEditorAction.SetResolveDate(invalidDate))

            //then
            with(controller.state.currentFormModel.resolveDate) {
                assertEquals(defect.issueDate, value)
                assertEquals(invalidDate, valueAsText)
                assertFalse(isValid)
            }
        }

        //when
        controller.triggerAction(DefectEditorAction.SetResolveDate("24.10.2026"))

        //then
        with(controller.state.currentFormModel.resolveDate) {
            assertEquals(LocalDate.of(2026, 10, 24), value)
            assertEquals("24.10.2026", valueAsText)
            assertTrue(isValid)
        }

        //when
        controller.triggerAction(DefectEditorAction.SetStatus(DefectState.OPEN))
        controller.triggerAction(DefectEditorAction.SetResolveDate("25.10.2026"))

        //then
        assertEquals("", controller.state.currentFormModel.resolveDate.valueAsText)
        assertNull(controller.state.currentFormModel.asDefect().resolveDate)
        assertEquals(defect, repository.readDefect(defect.id))
    }

    @Test
    fun testIssueDateRevalidatesResolveDate(){
        //given
        val defect = repository.create(Defect(id = -1, name = "Broken ramp",
                                             status = DefectState.DONE,
                                             issueDate = LocalDate.of(2026, 10, 20),
                                             resolveDate = LocalDate.of(2026, 10, 22), parkId = 5))
        val controller = DefectEditorController(defect.id, repository)

        //when
        controller.triggerAction(DefectEditorAction.SetIssueDate("23.10.2026"))

        //then
        with(controller.state.currentFormModel.resolveDate) {
            assertEquals(defect.resolveDate, value)
            assertEquals("22.10.2026", valueAsText)
            assertFalse(isValid)
        }

        //when
        controller.triggerAction(DefectEditorAction.SetIssueDate("21.10.2026"))

        //then
        with(controller.state.currentFormModel.resolveDate) {
            assertEquals(defect.resolveDate, value)
            assertEquals("22.10.2026", valueAsText)
            assertTrue(isValid)
        }

        //given
        controller.triggerAction(DefectEditorAction.SetResolveDate("19.10.2026"))
        assertFalse(controller.state.currentFormModel.resolveDate.isValid)

        //when
        controller.triggerAction(DefectEditorAction.SetIssueDate("18.10.2026"))

        //then
        with(controller.state.currentFormModel.resolveDate) {
            assertEquals(LocalDate.of(2026, 10, 19), value)
            assertEquals("19.10.2026", valueAsText)
            assertTrue(isValid)
        }
        assertEquals(LocalDate.of(2026, 10, 19), controller.state.currentFormModel.asDefect().resolveDate)
        assertEquals(defect, repository.readDefect(defect.id))
    }

    @Test
    fun testUpdate(){
        //given
        val defect = repository.create(Defect(id = -1, name = "Broken ramp",
                                             issueDate = LocalDate.of(2026, 10, 20), parkId = 5))
        var onFinishedCounter = 0
        val controller = DefectEditorController(defect.id, repository,
                                               onFinished = { onFinishedCounter++ })
        controller.triggerAction(DefectEditorAction.SetName(""))

        //when
        controller.triggerAction(DefectEditorAction.Update)

        //then
        assertFalse(controller.state.currentFormModel.isValid)
        assertEquals(defect, repository.readDefect(defect.id))
        assertEquals(0, onFinishedCounter)

        //given
        controller.triggerAction(DefectEditorAction.SetName("Repaired ramp"))
        controller.triggerAction(DefectEditorAction.SetStatus(DefectState.DONE))
        controller.triggerAction(DefectEditorAction.SetResolveDate("19.10.2026"))

        //when
        controller.triggerAction(DefectEditorAction.Update)

        //then
        assertFalse(controller.state.currentFormModel.isValid)
        assertEquals(defect, repository.readDefect(defect.id))
        assertEquals(0, onFinishedCounter)

        //given
        controller.triggerAction(DefectEditorAction.SetDescription("Board replaced"))
        controller.triggerAction(DefectEditorAction.SetIssueDate("21.10.2026"))
        controller.triggerAction(DefectEditorAction.SetResolveDate("22.10.2026"))
        controller.triggerAction(DefectEditorAction.SetParkId("7"))
        val updatedDefect = defect.copy(name = "Repaired ramp",
                                        description = "Board replaced",
                                        status = DefectState.DONE,
                                        issueDate = LocalDate.of(2026, 10, 21),
                                        resolveDate = LocalDate.of(2026, 10, 22), parkId = 7)

        //when
        controller.triggerAction(DefectEditorAction.Update)

        //then
        assertTrue(controller.state.currentFormModel.isValid)
        assertEquals(updatedDefect, repository.readDefect(defect.id))
        assertEquals(1, onFinishedCounter)
    }

    @Test
    fun testOnFinished(){
        //given
        val defect = repository.create(Defect(id = -1, name = "Broken ramp", parkId = 5))
        var onFinishedCounter = 0
        val controller = DefectEditorController(defect.id, repository,
                                               onFinished = { onFinishedCounter++ })
        controller.triggerAction(DefectEditorAction.SetName("Unsaved name"))

        //when
        controller.triggerAction(DefectEditorAction.Back)

        //then
        assertEquals(1, onFinishedCounter)
        assertEquals(defect, repository.readDefect(defect.id))
    }

    @Test
    fun testDelete(){
        //given
        val defect = repository.create(Defect(id = -1, name = "Broken ramp", parkId = 5))
        val otherDefect = repository.create(Defect(id = -1, name = "Broken bench", parkId = 7))
        var onFinishedCounter = 0
        val controller = DefectEditorController(defect.id, repository,
                                               onFinished = { onFinishedCounter++ })

        //when
        controller.triggerAction(DefectEditorAction.Delete)

        //then
        assertNull(repository.readDefect(defect.id))
        assertEquals(otherDefect, repository.readDefect(otherDefect.id))
        assertEquals(1, onFinishedCounter)
    }

    @Test
    fun testSaveBackToPark(){
        //given
        val defect = repository.create(Defect(id = -1, name = "Broken ramp", parkId = 5))
        var selectedParkId = -999
        var onParkSelectedCounter = 0
        var onFinishedCounter = 0
        val controller = DefectEditorController(defect.id, repository,
                                               onFinished = { onFinishedCounter++ },
                                               onSkateparkSelected = {
                                                   selectedParkId = it
                                                   onParkSelectedCounter++
                                               })
        controller.triggerAction(DefectEditorAction.SetName("Repaired ramp"))
        controller.triggerAction(DefectEditorAction.SetParkId("7"))

        //when
        controller.triggerAction(DefectEditorAction.SaveBackToPark)

        //then
        assertEquals(defect.copy(name = "Repaired ramp", parkId = 7), repository.readDefect(defect.id))
        assertEquals(7, selectedParkId)
        assertEquals(1, onParkSelectedCounter)
        assertEquals(0, onFinishedCounter)
    }

    @Test
    fun testSaveBackToParkRequiresValidFormAndPark(){
        //given
        val defect = repository.create(Defect(id = -1, name = "Broken ramp", parkId = -1))
        var onParkSelectedCounter = 0
        var onFinishedCounter = 0
        val controller = DefectEditorController(defect.id, repository,
                                               onFinished = { onFinishedCounter++ },
                                               onSkateparkSelected = { onParkSelectedCounter++ })
        controller.triggerAction(DefectEditorAction.SetName("Unsaved name"))

        //when
        controller.triggerAction(DefectEditorAction.SaveBackToPark)

        //then
        assertEquals(defect, repository.readDefect(defect.id))
        assertEquals(0, onParkSelectedCounter)
        assertEquals(0, onFinishedCounter)

        //given
        controller.triggerAction(DefectEditorAction.SetParkId("5"))
        controller.triggerAction(DefectEditorAction.SetName(""))

        //when
        controller.triggerAction(DefectEditorAction.SaveBackToPark)

        //then
        assertEquals(defect, repository.readDefect(defect.id))
        assertEquals(0, onParkSelectedCounter)
        assertEquals(0, onFinishedCounter)
    }
}