package yamert89.maxgrab

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.File
import kotlin.random.Random


class JsoupPageParser(private val domAlgorithm: JsoupDomAlgorithm): PageParser{
    override fun parse(url: String): List<HtmlRecord> {
        return listOf(HtmlRecord(1L, emptyList()))
    }

    private fun findParent(el1: Element, el2: Element): Element{
        for (i in el1.parents().lastIndex downTo 0){
            if (el1.parents()[i].hasSameValue(el2.parents()[i])) return el1
        }
        throw IllegalStateException("Parent not found")
    }




    override fun parse(file: File, inputIdentifiers: List<DOMIdentifier<*>>): List<HtmlRecord>{
        val document = Jsoup.parse(file)
        val htmlElements = domAlgorithm.getHtmlElements(document, inputIdentifiers)
        val record = HtmlRecord(0L, htmlElements)
        return listOf(record)
    }



}
