package module06.skateparksAdmin_V2.defectsadmin.defecteditor.controller

import module06.skateparksAdmin_V2.defectsadmin.defecteditor.model.DefectFormModel

data class DefectEditorState(val title : String,
                             val currentFormModel: DefectFormModel
)