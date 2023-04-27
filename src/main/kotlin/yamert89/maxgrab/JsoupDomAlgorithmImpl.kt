package yamert89.maxgrab

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import kotlin.random.Random

class JsoupDomAlgorithmImpl : JsoupDomAlgorithm {
    override fun getHtmlElements(document: Document, inputIdentifiers: List<DOMIdentifier<*>>): List<HtmlElement<*>> {
        val targetElements: List<Pair<Element, MutableList<Int>>> = inputIdentifiers.map { document.getElementsByClass(it.value.toString()).first()!! to mutableListOf<Int>() }
        if (targetElements.isEmpty()) throw IllegalStateException("Target elements not found")

        val elementsWithIndexes = mutableSetOf<Element>()

        fun operateParent(el: Element): Element {
            val targetElement = targetElements.find { it.first === el }!!
            val resEl = el.parents().first()!!
            val childIdx = resEl.children().indexOf(targetElement.first)
            if (!elementsWithIndexes.contains(targetElement.first)) targetElement.second.add(0, childIdx)
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
            var child1 = el1
            var child2 = el2
            while (!el1.hasSameValue(el2)){
                child1 = el1
                child2 = el2
                el1 = operateParent(child1)
                el2 = operateParent(child2)
            }
            elementsWithIndexes.add(child1)
            elementsWithIndexes.add(child2)
            idx++
        }

        val rootEl = el1
        return targetElements.convertToHtmlElements(rootEl)
    }

    private fun List<Pair<Element, MutableList<Int>>>.convertToHtmlElements(rootElement: Element): List<HtmlElement<String>> {
        val rootAddress = DomAddress(DOMIdentifier(rootElement.className(), DomIdentifierType.CLASS))
        val htmlElements: List<HtmlElement<String>> = this.map { el ->
            HtmlElement(
                Random.nextLong(),
                el.first.text(),
                DomAddress(DOMIdentifier(el.first.className(), DomIdentifierType.CLASS), rootAddress, el.second)
            )
        }
        return htmlElements
    }
}