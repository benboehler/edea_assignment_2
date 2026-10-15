package module06.skateparksAdmin_V2.defectsadmin.defectsadmin.controller

import module06.xtracted.controller.Controller

data class DefectState(val title            : String,
                       val activeController : Controller<*>)
