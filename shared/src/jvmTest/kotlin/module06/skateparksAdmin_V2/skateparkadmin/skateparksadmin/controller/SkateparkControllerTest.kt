package module06.skateparksAdmin_V2.skateparkadmin.skateparksadmin.controller

class SkateparkControllerTest {
    /*
    private lateinit var skateparkRepo : SkateparkRepository
    private lateinit var defaultSkatepark : Skatepark


    @BeforeEach
    fun setup(){
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
        //when
        val controller = SkateparkController(skateparkRepo)

        //then
        with(controller.state){
            assertTrue(activeController is SkateparkExplorerController)
        }
    }

    @Test
    fun testSwitchToEditor(){
        //given
        val skatepark  = skateparkRepo.create(defaultSkatepark)
        val controller = SkateparkController(skateparkRepo)

        //when
        controller.triggerAction(SkateparkAdminAction.SwitchToEditor(skatepark.id))

        //then
        with(controller.state){
            assertTrue(activeController is SkateparkEditorController)
            val editorController = activeController as SkateparkEditorController
            assertEquals(skatepark.id, editorController.state.formModel.id.value)
        }
    }

    @Test
    fun testSwitchToExplorer(){
        //given
        val skatepark  = skateparkRepo.create(defaultSkatepark)
        val controller = SkateparkController(skateparkRepo)
        controller.triggerAction(SkateparkAdminAction.SwitchToEditor(skatepark.id))
        val editorController = controller.state.activeController as SkateparkEditorController
        editorController.triggerAction(SkateparkEditorAction.SetName("new Name"))
        editorController.triggerAction(SkateparkEditorAction.Update)

        //when
        controller.triggerAction(SkateparkAdminAction.SwitchToExplorer)

        //then
        with(controller.state){
            assertTrue(activeController is SkateparkExplorerController)
            val explorer = activeController as SkateparkExplorerController
            assertEquals("new Name", explorer.state.allSkateparks[0].name)
        }
    }

    @Test
    fun testOnCreate(){
        //given
        val skatepark  = skateparkRepo.create(defaultSkatepark)
        val controller = SkateparkController(skateparkRepo)
        val explorer = controller.state.activeController as SkateparkExplorerController

        //when
        explorer.triggerAction(SkateparkExplorerAction.CreateSkatepark)

        //then
        with(controller.state){
            assertTrue(activeController is SkateparkEditorController)
        }

    }

    @Test
    fun testOnDelete(){
        //given
        val skatepark  = skateparkRepo.create(defaultSkatepark)
        val controller = SkateparkController(skateparkRepo)
        controller.triggerAction(SkateparkAdminAction.SwitchToEditor(skatepark.id))
        val editorController = controller.state.activeController as SkateparkEditorController

        //when
        editorController.triggerAction(SkateparkEditorAction.Delete)

        //then
        with(controller.state){
            assertTrue(activeController is SkateparkExplorerController)
            val explorer = activeController as SkateparkExplorerController
            assertTrue(explorer.state.allSkateparks.isEmpty())
        }

    }

     */


}