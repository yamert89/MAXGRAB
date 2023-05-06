package yamert89.maxgrab

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import yamert89.maxgrab.exceptions.ElementNotFoundException
import kotlin.random.Random

class JsoupDomAlgorithmImpl : JsoupDomAlgorithm {
    override fun findSourceHtmlElements(document: Document, inputIdentifiers: List<DOMIdentifier>): List<HtmlElement<*>> {
        val intermediateElements: List<IntermediateElement> = inputIdentifiers.map {
            val element = document.getElement(it)
            IntermediateElement(element, it )
        }

        if (intermediateElements.isEmpty()) throw ElementNotFoundException()

        val rootEl = findRootElement(intermediateElements)

        intermediateElements.forEach { intermediateElement ->
            intermediateElement.indexes.addAll(intermediateElement.element.fillIndexList(rootEl))
        }
        return intermediateElements.convertToHtmlElements(rootEl)
    }

    override fun findTreeHtmlElements(document: Document, inputIdentifiers: List<DOMIdentifier>): List<HtmlElement<*>> {
        val sourceHtmlElements = findSourceHtmlElements(document, inputIdentifiers)

        return emptyList()
    }

    private fun findRootElement(intermediateElements: List<IntermediateElement>): Element {
        fun operateParent(el: Element): Element {
            return el.parents().first()!!
        }

        var idx = 1
        var el1: Element = intermediateElements[0].element
        while (idx <= intermediateElements.lastIndex){
            el1 = intermediateElements[idx - 1].element
            var el2 = intermediateElements[idx].element
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

    private fun List<IntermediateElement>.convertToHtmlElements(rootElement: Element): List<HtmlElement<String>> {
        val rootAddress = DomAddress(DOMIdentifier(rootElement.className(), DomIdentifierType.CLASS))
        val htmlElements: List<HtmlElement<String>> = this.map { el ->
            HtmlElement(
                Random.nextLong(),
                el.element.text(),
                DomAddress(el.identifier, rootAddress, el.indexes)
            )
        }
        return htmlElements
    }

    private fun Document.getElement(identifier: DOMIdentifier): Element{
        val elements = when(identifier.type){
            DomIdentifierType.CLASS -> getElementsByClass(identifier.value)
            DomIdentifierType.ID -> Elements(getElementById(identifier.value))
            DomIdentifierType.TAG -> getElementsByTag(identifier.value)
        }
        if (elements.isEmpty() || elements.first() == null) throw ElementNotFoundException()
        if (elements.size > 1 && identifier.attributes != null) return getByAttr(identifier.attributes)
        return elements.first()!!
    }

    private fun Document.getByAttr(attributes: List<Pair<String, String?>>): Element {
        val attr = attributes.first()
        val elements = if (attr.second != null) getElementsByAttributeValue(attr.first, attr.second!!)
        else getElementsByAttribute(attr.first)
        if (elements.isEmpty()) throw ElementNotFoundException()
        if (elements.size > 1) return getByAttr(attributes.subList(1, attributes.lastIndex))
        return elements.first()!!
    }
}