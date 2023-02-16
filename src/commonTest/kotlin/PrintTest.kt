import net.sergeych.sprintf.sprintf
import kotlin.test.Test
import kotlin.test.assertEquals

class PrintTest
{
    @Test
    fun testPrint() {
//        assertEquals("== 3 ==", "== %d ==".sprintf(3))
        assertEquals("01:00","%02d:%02d".sprintf(1,0))
    }
}