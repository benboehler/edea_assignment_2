package module06.skateparksAdmin_V2.defectsadmin.defectexplorer.controller

import java.time.LocalDate
import module06.skateparksAdmin_V2.defectsadmin.shared.Defect
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectRepository
import module06.xtracted.repository.urlFromResources
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefectExplorerControllerTest {
    private lateinit var repository: DefectRepository
    private val defaultDefect = Defect(id = -1, name = "Broken ramp",
                                      description = "Loose board",
                                      issueDate = LocalDate.of(2026, 10, 27), parkId = 5)

    @BeforeEach
    fun setup() {
        repository = DefectRepository("/data/defectDB".urlFromResources())
        repository.deleteAllDefects()
    }

    @Test
    fun testInitialization() {
        //given
        val first = repository.create(defaultDefect)
        val second = repository.create(defaultDefect.copy(parkId = 7))

        //when
        val controller = DefectExplorerController(repository = repository)

        //then
        assertEquals(2, controller.state.allDefects.size)
        assertTrue(controller.state.allDefects.contains(first))
        assertTrue(controller.state.allDefects.contains(second))
    }

    @Test
    fun testInitializationWithParkFilter() {
        //given
        val defect = repository.create(defaultDefect)
        repository.create(defaultDefect.copy(parkId = 7))

        //when
        val controller = DefectExplorerController(parkId = 5, repository = repository)

        //then
        assertEquals(1, controller.state.allDefects.size)
        assertTrue(controller.state.allDefects.contains(defect))
    }

    @Test
    fun testReload() {
        //given
        val controller = DefectExplorerController(repository = repository)
        assertTrue(controller.state.allDefects.isEmpty())
        val defect = repository.create(defaultDefect)

        //when
        controller.triggerAction(DefectExplorerAction.ReloadAllDefects)

        //then
        assertEquals(1, controller.state.allDefects.size)
        assertTrue(controller.state.allDefects.contains(defect))

        //when
        repository.deleteDefect(defect.id)
        controller.triggerAction(DefectExplorerAction.ReloadAllDefects)

        //then
        assertTrue(controller.state.allDefects.isEmpty())
    }

    @Test
    fun testReloadPreservesParkFilter() {
        //given
        val controller = DefectExplorerController(parkId = 5, repository = repository)
        val defect = repository.create(defaultDefect)
        repository.create(defaultDefect.copy(parkId = 7))

        //when
        controller.triggerAction(DefectExplorerAction.ReloadAllDefects)

        //then
        assertEquals(1, controller.state.allDefects.size)
        assertTrue(controller.state.allDefects.contains(defect))
    }

    @Test
    fun testOnCreate() {
        //given
        var createCounter = 0
        val controller = DefectExplorerController(repository = repository,
                                                  onCreate = { createCounter++ })

        //when
        controller.triggerAction(DefectExplorerAction.CreateDefect)

        //then
        assertEquals(1, createCounter)
        assertEquals(0, repository.countDefects())
    }

    @Test
    fun testOnSelect() {
        //given
        val defect = repository.create(defaultDefect)
        var selectedDefectId = -999
        val controller = DefectExplorerController(repository = repository,
                                                  onDefectSelected = { selectedDefectId = it })

        //when
        controller.triggerAction(DefectExplorerAction.SelectDefect(defect.id))

        //then
        assertEquals(defect.id, selectedDefectId)
    }
}