package module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.controller

import module06.skateparksAdmin_V2.defectsadmin.shared.DefectRepository
import module06.skateparksAdmin_V2.defectsadmin.shared.Defect
import module06.skateparksAdmin_V2.skateparkadmin.shared.Skatepark
import module06.skateparksAdmin_V2.skateparkadmin.shared.SkateparkRepository
import module06.skateparksAdmin_V2.skateparkadmin.shared.SkateparkState
import module06.xtracted.repository.urlFromResources
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SkateparkEditorControllerTest {

    private lateinit var skateparkRepo    : SkateparkRepository
    private lateinit var defectsRepo      : DefectRepository
    private lateinit var defaultSkatepark : Skatepark

    @BeforeEach
    fun setup(){
        skateparkRepo = SkateparkRepository("/data/skateparkDB".urlFromResources())
        defectsRepo   = DefectRepository("/data/defectDB".urlFromResources())

        skateparkRepo.deleteAllSkateparks()
        defectsRepo.deleteAllDefects()

        val name         = "Tony Hawk Park"
        val description  = SkateparkState.UNDER_CONSTRUCTION
        val street       = "Parkstrasse 73"
        val municipality = "5210 Windisch"
        val mapsUrl      = ""
        val latitude     = 48.123
        val longitude    = null
        val website      = ""
        val imageUrl     = ""
        val placeid      = ""

        defaultSkatepark = Skatepark(-1, name, description, street, municipality, mapsUrl, latitude, longitude, website, imageUrl, placeid)
    }

    @Test
    fun testInitialization(){
        //given
        val skatepark = skateparkRepo.create(defaultSkatepark)

        //when
        val controller = SkateparkEditorController(skatepark.id, skateparkRepo)

        //then
        with(controller.state){
            assertEquals(skatepark, formModel.asSkatepark())
            assertTrue(parkDefects.isEmpty())
            assertEquals(controller.defaultImageBitmap, imageBitmap)
        }
    }


    @Test
    fun testSetLongitude(){
        //given
        val skatepark = skateparkRepo.create(defaultSkatepark)
        val controller = SkateparkEditorController(skatepark.id, skateparkRepo)
        val newLongitude = "-123.45"

        //when
        controller.triggerAction(SkateparkEditorAction.SetLongitude(newLongitude))

        //then
        with(controller.state.formModel.longitude){
            assertEquals(newLongitude.toDouble(), value)
            assertEquals(newLongitude, valueAsText)
            assertTrue(isValid)
        }

        listOf("-181.0", "181.0").forEach { invalidLongitude ->
            controller.triggerAction(SkateparkEditorAction.SetLongitude(invalidLongitude))

            with(controller.state.formModel.longitude){
                assertEquals(newLongitude.toDouble(), value)
                assertEquals(invalidLongitude, valueAsText)
                assertFalse(isValid)
            }
        }

        listOf("-180.0", "180.0", "0.0").forEach { validLongitude ->
            controller.triggerAction(SkateparkEditorAction.SetLongitude(validLongitude))

            with(controller.state.formModel.longitude){
                assertEquals(validLongitude.toDouble(), value)
                assertEquals(validLongitude, valueAsText)
                assertTrue(isValid)
            }
        }

        controller.triggerAction(SkateparkEditorAction.SetLongitude(""))

        with(controller.state.formModel.longitude){
            assertEquals(0.0, value)
            assertEquals("", valueAsText)
            assertTrue(isValid)
        }

        controller.triggerAction(SkateparkEditorAction.SetLongitude("x"))

        with(controller.state.formModel.longitude){
            assertEquals(0.0, value)
            assertEquals("x", valueAsText)
            assertFalse(isValid)
        }
    }

    @Test
    fun testReload(){
        //given
        val skatepark = skateparkRepo.create(defaultSkatepark)
        val controller = SkateparkEditorController(skatepark.id, skateparkRepo)
        controller.triggerAction(SkateparkEditorAction.SetName("Unsaved name"))
        controller.triggerAction(SkateparkEditorAction.SetName(""))
        controller.triggerAction(SkateparkEditorAction.SetLatitude("x"))

        val updatedPark = skatepark.copy(name = "Name on database", latitude = 47.0)
        skateparkRepo.updateSkatepark(updatedPark)

        //when
        controller.triggerAction(SkateparkEditorAction.Reload)

        //then
        assertEquals(updatedPark, controller.state.formModel.asSkatepark())
        assertTrue(controller.state.formModel.name.isValid)
        assertTrue(controller.state.formModel.latitude.isValid)
        assertEquals(updatedPark, skateparkRepo.readSkatepark(skatepark.id))
    }

    @Test
    fun testOnFinished(){
        //given
        val skatepark = skateparkRepo.create(defaultSkatepark)
        var onFinishedCounter = 0
        val controller = SkateparkEditorController(skatepark.id, skateparkRepo,
                                                   onFinished = { onFinishedCounter++ })
        controller.triggerAction(SkateparkEditorAction.SetName("Unsaved name"))

        //when
        controller.triggerAction(SkateparkEditorAction.Back)

        //then
        assertEquals(1, onFinishedCounter)
        assertEquals(skatepark, skateparkRepo.readSkatepark(skatepark.id))
    }


    @Test
    fun testSetName(){
        //given
        val skatepark = skateparkRepo.create(defaultSkatepark)
        val controller = SkateparkEditorController(skatepark.id, skateparkRepo)
        val newName = "Tony Hawks memorial stadium"

        //when
        controller.triggerAction(SkateparkEditorAction.SetName(newName))

        //then
        with(controller.state.formModel.name){
            assertEquals(newName, value)
            assertEquals(newName, valueAsText)
            assertTrue(isValid)
        }

        //when
        controller.triggerAction(SkateparkEditorAction.SetName(""))

        //then
        with(controller.state.formModel.name){
            assertEquals(newName, value)
            assertEquals("", valueAsText)
            assertFalse(isValid)
        }
    }

    @Test
    fun testSetLatitude(){
        //given
        val skatepark = skateparkRepo.create(defaultSkatepark)
        val controller = SkateparkEditorController(skatepark.id, skateparkRepo)
        val newLatitude = "-12.34"

        //when
        controller.triggerAction(SkateparkEditorAction.SetLatitude(newLatitude))

        //then
        with(controller.state.formModel.latitude){
            assertEquals(newLatitude.toDouble(), value)
            assertEquals(newLatitude, valueAsText)
            assertTrue(isValid)
        }

        //when
        controller.triggerAction(SkateparkEditorAction.SetLatitude("-91.0"))

        //then
        with(controller.state.formModel.latitude){
            assertEquals(newLatitude.toDouble(), value)
            assertEquals("-91.0", valueAsText)
            assertFalse(isValid)
        }

        //when
        controller.triggerAction(SkateparkEditorAction.SetLatitude("91.0"))

        //then
        with(controller.state.formModel.latitude){
            assertEquals(newLatitude.toDouble(), value)
            assertEquals("91.0", valueAsText)
            assertFalse(isValid)
        }

        //when
        controller.triggerAction(SkateparkEditorAction.SetLatitude(""))

        //then
        with(controller.state.formModel.latitude){
            assertEquals(0.0, value)
            assertEquals("", valueAsText)
            assertTrue(isValid)
        }


        //when
        controller.triggerAction(SkateparkEditorAction.SetLatitude("x"))

        //then
        with(controller.state.formModel.latitude){
            assertEquals(0.0, value)
            assertEquals("x", valueAsText)
            assertFalse(isValid)
        }
    }

    @Test
    fun testUpdate(){
        //given
        val skatepark = skateparkRepo.create(defaultSkatepark)
        var onFinishedCounter = 0;
        val controller = SkateparkEditorController(skatepark.id, skateparkRepo,
                                                   onFinished = {onFinishedCounter++})
        val newName = "Tony Hawks memorial stadium"
        controller.triggerAction(SkateparkEditorAction.SetName(newName))

        //when
        controller.triggerAction(SkateparkEditorAction.Update)

        //then
        assertEquals(newName, skateparkRepo.readSkatepark(skatepark.id)?.name)
        assertEquals(1, onFinishedCounter)
    }

    @Test
    fun testDelete(){
        //given
        val skatepark = skateparkRepo.create(defaultSkatepark)
        var onFinishedCounter = 0;
        val controller = SkateparkEditorController(skatepark.id, skateparkRepo,
                                                   onFinished = {onFinishedCounter++})

        //when
        controller.triggerAction(SkateparkEditorAction.Delete)

        //then
        assertNull(skateparkRepo.readSkatepark(skatepark.id))
        assertEquals(1, onFinishedCounter)
    }

    @Test
    fun testDeleteWithDefects(){
        //given
        val skatepark = skateparkRepo.create(defaultSkatepark)
        val defect = defectsRepo.create(Defect(id = -1, parkId = skatepark.id))

        val controller = SkateparkEditorController(skatepark.id, skateparkRepo)

        //when
        controller.triggerAction(SkateparkEditorAction.Delete)

        //then
        assertNull(defectsRepo.readDefect(defect.id))

    }

}