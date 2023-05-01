package yamert89.maxgrab

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import kotlin.random.Random

class JsoupDomAlgorithmImpl : JsoupDomAlgorithm {
    override fun findSourceHtmlElements(document: Document, inputIdentifiers: List<DOMIdentifier<*>>): List<HtmlElement<*>> {
        val targetElements: List<Pair<Element, MutableList<Int>>> = inputIdentifiers.map { document.getElementsByClass(it.value.toString()).first()!! to mutableListOf<Int>() }
        if (targetElements.isEmpty()) throw IllegalStateException("Target elements not found")

        val rootEl = findRootElement(targetElements)

        targetElements.forEach { targetElement ->
            targetElement.second.addAll(targetElement.first.fillIndexList(rootEl))
        }
        return targetElements.convertToHtmlElements(rootEl)
    }

    override fun findTreeHtmlElements(document: Document, inputIdentifiers: List<DOMIdentifier<String>>): List<HtmlElement<*>> {
        return emptyList()
    }

    private fun findRootElement(targetElements: List<Pair<Element, MutableList<Int>>>): Element {
        fun operateParent(el: Element): Element {
            return el.parents().first()!!
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
        return el1
    }

    private fun Element.fillIndexList(root: Element): List<Int> {
        val list = mutableListOf<Int>()
        var parent = this
        var el = this
        while (parent != root){
            parent = el.parent()!!
            list.add(0, parent.children().indexOf(el))
            el = parent
        }
        return list
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