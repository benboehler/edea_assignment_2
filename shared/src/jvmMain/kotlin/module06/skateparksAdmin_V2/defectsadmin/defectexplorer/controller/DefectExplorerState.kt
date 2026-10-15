package module06.skateparksAdmin_V2.defectsadmin.defectexplorer.controller

import module06.skateparksAdmin_V2.defectsadmin.shared.Defect

data class DefectExplorerState(val title : String,
                               val allDefects : List<Defect>)