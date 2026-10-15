package module06.skateparksAdmin_V2.skateparkadmin.shared

import module06.xtracted.repository.urlFromResources
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SkateparkRepositoryTest {
    private val url = "/data/skateparkDB".urlFromResources()
    private lateinit var repository: SkateparkRepository

    @BeforeEach
    fun setUp() {
        repository = SkateparkRepository(url)
        repository.deleteAllSkateparks()
    }

    @Test
    fun create() {
        //given
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

        val initial = Skatepark(-1, name, description, street, municipality, mapsUrl, latitude, longitude, website, imageUrl, placeid)

        //when
        val parkOnDB = repository.create(initial)

        //then
        assertEquals(1, repository.countSkateparks())
        assertEquals(parkOnDB, repository.readSkatepark(parkOnDB.id))
    }

    @Test
    fun readSkatepark() {
        //given
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

        val initial = Skatepark(-1, name, description, street, municipality, mapsUrl, latitude, longitude, website, imageUrl, placeid)

        val parkToBeSaved = repository.create(initial)

        //when
        val park = repository.readSkatepark(parkToBeSaved.id)!!

        //then
        assertEquals(parkToBeSaved, park)
        // just to be sure that null value is handeled correct
        assertNull(park.longitude)
    }

    @Test
    fun readAllSkateparks() {
        //then
        assertTrue(repository.readAllSkateparks().isEmpty())

        //when
        val park1 = repository.create(Skatepark())

        //then
        val allSkateparks = repository.readAllSkateparks()
        assertEquals(1, allSkateparks.size)
        assertEquals(park1, allSkateparks[0])
    }

    @Test
    fun updateSkatepark() {
        //given
        val park = repository.create(Skatepark())

        val updatedPark = park.copy(name = "new",
                                    description = SkateparkState.OPERATIONAL,
                                    latitude = 48.12)

        //when
        repository.updateSkatepark(updatedPark)

        //then
        val parkOnDb = repository.readSkatepark(park.id)!!
        assertEquals(updatedPark, parkOnDb)
    }

    @Test
    fun deleteSkatepark() {
        //given
        val park = repository.create(Skatepark())

        //when
        repository.deleteSkatepark(park.id)

        //then
        assertNull(repository.readSkatepark(park.id))
    }

    @Test
    fun deleteAllSkateparks() {
        //given
        repository.create(Skatepark())
        repository.create(Skatepark())

        //when
        repository.deleteAllSkateparks()

        //then
        assertEquals(0, repository.countSkateparks())
        assertTrue(repository.readAllSkateparks().isEmpty())
    }

    @Test
    fun countSkateparks() {
        //then
        assertEquals(0, repository.countSkateparks())

        //given
        repository.create(Skatepark())

        //then
        assertEquals(1, repository.countSkateparks())
    }

}