package module06.skateparksAdmin_V2.fedAdmin.controller

import module06.xtracted.controller.Controller

data class FedAdminState(val title : String,
                         val activeController: Controller<*>)