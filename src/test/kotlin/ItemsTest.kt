import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import yamert89.maxgrab.*
import java.io.File
import kotlin.test.assertEquals

class ItemsTest {
    lateinit var address: DomAddress
    @BeforeEach
    fun setup(){
        val identifier = DOMIdentifier("css-class", DomIdentifierType.CLASS)
        val indexes = listOf(0, 0, 0)
        address = DomAddress(identifier, DomAddress(identifier), indexes)
    }

    @Test
    fun `new htmlElement`(){
        val htmlElement = HtmlElement(1, "simple", address)
        assertEquals("simple", htmlElement.value)
        assertEquals(1, htmlElement.id)
    }

    @Test
    fun `new record`(){
        val htmlElement = HtmlElement(1L, "1", address)
        val htmlElement2 = HtmlElement(2L, "2", address)
        val htmlRecord = HtmlRecord(1L, listOf(htmlElement, htmlElement2))
        assertEquals(listOf(htmlElement, htmlElement2), htmlRecord.elements)
        assertEquals(1L, htmlRecord.id)
    }

    @Test
    fun `children in parent`(){
        val rootAddress = DomAddress(DOMIdentifier("root", DomIdentifierType.CLASS))
        val address1 = DomAddress(DOMIdentifier("child", DomIdentifierType.CLASS), rootAddress, listOf(1))
        val address2 = DomAddress(DOMIdentifier("child", DomIdentifierType.CLASS), rootAddress, listOf(2))
        val child1 = HtmlElement(1, "a", address1)
        val child2 = HtmlElement(1, "a", address2)
        assertEquals(rootAddress, child1.address!!.rootAddress)
        assertEquals(rootAddress, child2.address!!.rootAddress)
    }

    @Test
    fun `children in parent html1`(){
        val pageParser = JsoupPageParser()
        val inputDOMIdentifier1 = DOMIdentifier("c1", DomIdentifierType.CLASS)
        val inputDOMIdentifier2 = DOMIdentifier("c3", DomIdentifierType.CLASS)
        val records: List<HtmlRecord> = pageParser.parse(
            resourceFile("1.html"),
            listOf(inputDOMIdentifier1, inputDOMIdentifier2)
        )
        val elements = mutableListOf<HtmlElement<String>>()
        val rootAddress = DomAddress(DOMIdentifier("root", DomIdentifierType.CLASS))
        elements.add(HtmlElement(1L, "text", DomAddress(DOMIdentifier("c1", DomIdentifierType.CLASS), rootAddress, listOf(0))))
        //elements.add(HtmlElement(2L, "0.5", DomAddress(DOMIdentifier("c2", DomIdentifierType.CLASS), rootAddress, listOf(1))))
        elements.add(HtmlElement(3L, "145", DomAddress(DOMIdentifier("c3", DomIdentifierType.CLASS), rootAddress, listOf(2))))
        val record = HtmlRecord(0L, elements)
        assertEquals(record, records.first())
    }

    private fun resourceFile(fileName: String): File{
        val fileName1 = ItemsTest::class.java.getResource(fileName).file
        val file = File(fileName1)
        return file
    }




}