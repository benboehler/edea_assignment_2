package module06.skateparksAdmin_V2.skateparkadmin.skateparkexplorer.controller

import module06.skateparksAdmin_V2.skateparkadmin.shared.Skatepark
import module06.skateparksAdmin_V2.skateparkadmin.shared.SkateparkRepository
import module06.skateparksAdmin_V2.skateparkadmin.shared.SkateparkState
import module06.xtracted.repository.urlFromResources
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SkateparkExplorerControllerTest {
    private lateinit var skateparkRepo    : SkateparkRepository
    private lateinit var defaultSkatepark : Skatepark

    @BeforeEach
    fun setup() {
        skateparkRepo = SkateparkRepository("/data/skateparkDB".urlFromResources())

        skateparkRepo.deleteAllSkateparks()

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
        val skatepark1 = skateparkRepo.create(defaultSkatepark)
        val skatepark2 = skateparkRepo.create(defaultSkatepark)

        //when
        val controller = SkateparkExplorerController(skateparkRepo)

        //then
        with(controller.state){
            assertEquals(2, allSkateparks.size)
            assertTrue(allSkateparks.contains(skatepark1))
            assertTrue(allSkateparks.contains(skatepark2))
        }
    }

    @Test
    fun testReload(){
        //given
        val controller = SkateparkExplorerController(skateparkRepo)

        //then
        assertTrue(controller.state.allSkateparks.isEmpty())

        //when
        val skatepark = skateparkRepo.create(defaultSkatepark)
        controller.triggerAction(SkateparkExplorerAction.ReloadAllSkateparks)

        //then
        with(controller.state){
            assertEquals(1, allSkateparks.size)
            assertTrue(allSkateparks.contains(skatepark))
        }
    }

    @Test
    fun testOnCreate(){
        //given
        var createCounter = 0
        val controller = SkateparkExplorerController(skateparkRepo,
                                                     onCreate = { createCounter++})

        //when
        controller.triggerAction(SkateparkExplorerAction.CreateSkatepark)

        //then
        assertEquals(1, createCounter)
    }

    @Test
    fun testOnSelect(){
        //given
        val skatepark = skateparkRepo.create(defaultSkatepark)
        var selectedParkId = -999
        val controller = SkateparkExplorerController(skateparkRepo,
                                                     onSkateparkSelected = { selectedParkId = it})

        //when
        controller.triggerAction(SkateparkExplorerAction.SelectSkatepark(skatepark.id))

        //then
        assertEquals(skatepark.id, selectedParkId)
    }



}