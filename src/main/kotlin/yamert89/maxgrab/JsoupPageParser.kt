package yamert89.maxgrab

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.File
import kotlin.random.Random


class JsoupPageParser: PageParser{
    override fun parse(url: String): List<HtmlRecord> {
        return listOf(HtmlRecord(1L, emptyList()));
    }

    private fun findParent(el1: Element, el2: Element): Element{
        for (i in el1.parents().lastIndex downTo 0){
            if (el1.parents()[i].hasSameValue(el2.parents()[i])) return el1
        }
        throw IllegalStateException("Parent not found")
    }




    override fun parse(file: File, inputIdentifiers: List<DOMIdentifier<*>>): List<HtmlRecord>{
        val document = Jsoup.parse(file)
        val targetElements: List<Pair<Element, MutableList<Int>>> = inputIdentifiers.map { document.getElementsByClass(it.value.toString()).first()!! to mutableListOf<Int>() }
        if (targetElements.isEmpty()) throw IllegalStateException("Target elements not found")

        fun operateParent(el: Element): Element{
            val targetElement = targetElements.find { it.first === el }!!
            val resEl = el.parents().first()!!
            val childIdx = resEl.children().indexOf(targetElement.first)
            targetElement.second.add(0, childIdx)
            return resEl
        }

        var idx = 1
        var el1: Element = targetElements[0].first
        while (idx <= targetElements.lastIndex){
            el1 = targetElements[idx - 1].first
            var el2 = targetElements[idx].first
            var h1 = el1.parents().size
            var h2 = el2.parents().size
            while (h1 != h2){
                if (h1 > h2){
                    el1 = operateParent(el1)
                    h1--
                } else {
                    el2 = operateParent(el2)
                    h2--
                }
            }
            while (!el1.hasSameValue(el2)){
                el1 = operateParent(el1)
                el2 = operateParent(el2)
            }
            idx++
        }

        val rootEl = el1
        val htmlElements = targetElements.convertToHtmlElements(rootEl)
        val record = HtmlRecord(0L, htmlElements)
        return listOf(record)
    }

    private fun List<Pair<Element, MutableList<Int>>>.convertToHtmlElements(rootElement: Element): List<HtmlElement<Int, String>> {
        val rootAddress = DomAddress(DOMIdentifier(rootElement.className(), DomIdentifierType.CLASS))
        val htmlElements: List<HtmlElement<Int, String>> = this.map { el ->
            HtmlElement(
                Random.nextInt(),
                el.first.text(),
                DomAddress(DOMIdentifier(el.first.className(), DomIdentifierType.CLASS), rootAddress, el.second)
            )
        }
        return htmlElements
    }

}
