package module06.skateparksAdmin_V2.defectsadmin.defecteditor.model

import java.time.LocalDate
import module06.skateparksAdmin_V2.defectsadmin.shared.Defect
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectState
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefectFormModelTest {
    @Test
    fun testInitialization(){
        //given
        val defect = Defect(id = 14,
                            name = "Damaged ramp",
                            description = "Loose board",
                            status = DefectState.OPEN,
                            issueDate = LocalDate.of(2026, 10, 19),
                            resolveDate = null,
                            parkId = 5)

        //when
        val formModel = DefectFormModel(defect)

        //then
        assertEquals(defect, formModel.asDefect())
        assertEquals("19.10.2026", formModel.issueDate.valueAsText)
        assertEquals("", formModel.resolveDate.valueAsText)
        assertEquals(DefectState.OPEN.description, formModel.status.valueAsText)
        assertTrue(formModel.resolveDate.isValid)

        //given
        val resolvedDefect = defect.copy(status = DefectState.DONE,
                                         resolveDate = LocalDate.of(2026, 10, 22))

        //when
        val resolvedFormModel = DefectFormModel(resolvedDefect)

        //then
        assertEquals(resolvedDefect, resolvedFormModel.asDefect())
        assertEquals("22.10.2026", resolvedFormModel.resolveDate.valueAsText)
        assertEquals(DefectState.DONE.description, resolvedFormModel.status.valueAsText)
    }

}