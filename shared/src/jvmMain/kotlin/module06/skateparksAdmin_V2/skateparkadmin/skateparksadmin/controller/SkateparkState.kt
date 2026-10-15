package module06.skateparksAdmin_V2.skateparkadmin.skateparksadmin.controller

import module06.xtracted.controller.Controller

data class SkateparkState(val title            : String,
                       val activeController : Controller<*>
                          )
