import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import yamert89.maxgrab.*
import yamert89.maxgrab.exceptions.ElementNotFoundException
import java.io.File
import kotlin.jvm.Throws
import kotlin.test.assertEquals

class ParserTest {
    lateinit var address: DomAddress

    private fun resourceFile(fileName: String): File{
        val fileName1 = ParserTest::class.java.getResource(fileName).file
        val file = File(fileName1)
        return file
    }

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
        val pageParser = JsoupPageParser(JsoupDomAlgorithmImpl())
        val inputDOMIdentifier1 = DOMIdentifier("c1", DomIdentifierType.CLASS)
        val inputDOMIdentifier2 = DOMIdentifier("c2", DomIdentifierType.CLASS)
        val inputDOMIdentifier3 = DOMIdentifier("c3", DomIdentifierType.CLASS)
        val parsedElements = pageParser.parseSourceElements(
            resourceFile("1.html"),
            listOf(inputDOMIdentifier1, inputDOMIdentifier2, inputDOMIdentifier3)
        )
        val elements = mutableListOf<HtmlElement<String>>()
        val rootAddress = DomAddress(DOMIdentifier("root", DomIdentifierType.CLASS))
        elements.add(HtmlElement(1L, "text", DomAddress(DOMIdentifier("c1", DomIdentifierType.CLASS), rootAddress, listOf(0))))
        elements.add(HtmlElement(2L, "0.5", DomAddress(DOMIdentifier("c2", DomIdentifierType.CLASS), rootAddress, listOf(1))))
        elements.add(HtmlElement(3L, "145", DomAddress(DOMIdentifier("c3", DomIdentifierType.CLASS), rootAddress, listOf(2))))
        assertEquals(elements, parsedElements)
    }

    @Test
    fun `children in parent html2`(){
        val pageParser = JsoupPageParser(JsoupDomAlgorithmImpl())
        val inputDOMIdentifier1 = DOMIdentifier("c1", DomIdentifierType.CLASS)
        val inputDOMIdentifier2 = DOMIdentifier("c2", DomIdentifierType.CLASS)
        val inputDOMIdentifier3 = DOMIdentifier("c3", DomIdentifierType.CLASS)
        val parsedElements = pageParser.parseSourceElements(
            resourceFile("2.html"),
            listOf(inputDOMIdentifier1, inputDOMIdentifier2, inputDOMIdentifier3)
        )
        val elements = mutableListOf<HtmlElement<String>>()
        val rootAddress = DomAddress(DOMIdentifier("root", DomIdentifierType.CLASS))
        elements.add(HtmlElement(1L, "text", DomAddress(DOMIdentifier("c1", DomIdentifierType.CLASS), rootAddress, listOf(0))))
        elements.add(HtmlElement(2L, "0.5", DomAddress(DOMIdentifier("c2", DomIdentifierType.CLASS), rootAddress, listOf(1, 0, 0))))
        elements.add(HtmlElement(3L, "145", DomAddress(DOMIdentifier("c3", DomIdentifierType.CLASS), rootAddress, listOf(2, 1))))
        assertEquals(elements, parsedElements)
    }

    @Test
    fun elementNotFound(){
        val pageParser = JsoupPageParser(JsoupDomAlgorithmImpl())
        val inputDOMIdentifier1 = DOMIdentifier("not found", DomIdentifierType.ID)
        assertThrows<ElementNotFoundException> {
            pageParser.parseTree(resourceFile("identifier.html"),
                listOf(inputDOMIdentifier1)
            )
        }
    }

    @Test
    fun identifiers(){ //todo root identifier = tag
        val inputDOMIdentifier1 = DOMIdentifier("id1", DomIdentifierType.ID)
        val inputDOMIdentifier2 = DOMIdentifier("id2", DomIdentifierType.ID)
        val inputDOMIdentifier3 = DOMIdentifier("cl1", DomIdentifierType.CLASS)
        val inputDOMIdentifier4 = DOMIdentifier("a", DomIdentifierType.TAG, listOf("href" to "http://yandex.ru"))
        val rootIdentifier = DOMIdentifier("root", DomIdentifierType.CLASS)
        val elements = mutableListOf<HtmlElement<String>>()
        val rootAddress = DomAddress(rootIdentifier)
        elements.add(HtmlElement(1L, "text", DomAddress(inputDOMIdentifier1, rootAddress, listOf(1))))
        elements.add(HtmlElement(2L, "0.5", DomAddress(inputDOMIdentifier2, rootAddress, listOf(2,0))))
        elements.add(HtmlElement(3L, "cl1", DomAddress(inputDOMIdentifier3, rootAddress, listOf(2,1))))
        elements.add(HtmlElement(4L, "simple link", DomAddress(inputDOMIdentifier4, rootAddress, listOf(3))))
        val pageParser = JsoupPageParser(JsoupDomAlgorithmImpl())
        val parsedElements = pageParser.parseSourceElements(resourceFile("identifier.html"),
            listOf(inputDOMIdentifier1, inputDOMIdentifier2, inputDOMIdentifier3, inputDOMIdentifier4)
        )
        assertEquals(elements, parsedElements)
    }

    @Test
    fun t(){
        val pageParser = JsoupPageParser(JsoupDomAlgorithmImpl())
        val inputDOMIdentifier1 = DOMIdentifier("c1", DomIdentifierType.CLASS)
        val inputDOMIdentifier2 = DOMIdentifier("c2", DomIdentifierType.CLASS)
        val rootAddress = DomAddress(DOMIdentifier("root", DomIdentifierType.CLASS))
        val elements = mutableListOf<HtmlElement<String>>()
        elements.add(HtmlElement(1L, "0.1", DomAddress(DOMIdentifier("c1", DomIdentifierType.CLASS), rootAddress, listOf(1,0,0))))
        elements.add(HtmlElement(2L, "0.2", DomAddress(DOMIdentifier("c2", DomIdentifierType.CLASS), rootAddress, listOf(2,0,0))))
        elements.add(HtmlElement(3L, "0.3", DomAddress(DOMIdentifier("c3", DomIdentifierType.CLASS), rootAddress, listOf(3,0,0))))
        elements.add(HtmlElement(4L, "0.4", DomAddress(DOMIdentifier("c4", DomIdentifierType.CLASS), rootAddress, listOf(4,0,0))))
        val parsedElements = pageParser.parseTree(resourceFile("3.html"),
            listOf(inputDOMIdentifier1, inputDOMIdentifier2)
        )
        assertEquals(elements, elements)
    }






}