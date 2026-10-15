package module06.skateparksAdmin_V2.skateparkadmin.skateparkexplorer.controller

import module06.skateparksAdmin_V2.skateparkadmin.shared.Skatepark

data class SkateparkExplorerState(val title : String,
                               val allSkateparks : List<Skatepark>)