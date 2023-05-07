package yamert89.maxgrab

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import yamert89.maxgrab.TreeAlgorithm.*
import yamert89.maxgrab.exceptions.ElementNotFoundException
import yamert89.maxgrab.exceptions.SeveralElementsException
import kotlin.random.Random

class JsoupDomAlgorithmImpl(private val document: Document) : JsoupDomAlgorithm {
    override fun findSourceHtmlElements(inputIdentifiers: List<DOMIdentifier>): List<HtmlElement<*>> {
        val intermediateElements: List<IntermediateElement> = inputIdentifiers.convertToIntermediate()

        if (intermediateElements.isEmpty()) throw ElementNotFoundException()

        val rootEl = findRootElement(intermediateElements)

        intermediateElements.forEach { intermediateElement ->
            intermediateElement.indexes.addAll(intermediateElement.element.fillIndexList(rootEl))
        }
        return intermediateElements.convertToHtmlElements(rootEl)
    }

    override fun findTreeHtmlElements(exampleIdentifiers: List<DOMIdentifier>, treeAlgorithm: TreeAlgorithm): List<HtmlElement<*>> {

        val elements: List<HtmlElement<*>> = when(treeAlgorithm){
            CLASSIFIED -> {

                val intermediateElements: List<IntermediateElement> = exampleIdentifiers.convertToIntermediate()
                val rootEl = findRootElement(intermediateElements)
                val identifiers = intermediateElements.map { it.identifier }
                val identifiersSet = identifiers.toSet()
                if (identifiersSet.size == 1){
                    val domElements = rootEl.getElementsByDomIdentifier(identifiersSet.first())
                    domElements.map {
                        HtmlElement(0L, it.text(), it.createDomAddress(rootEl))
                    }
                } else emptyList<HtmlElement<*>>() //TODO redirect unique?

            }
            UNIQUE -> {

                emptyList<HtmlElement<*>>() //TODO
            }
            PARENT_CLASSIFIED -> {
                emptyList<HtmlElement<*>>() //TODO
            }
        }


        val sourceHtmlElements = findSourceHtmlElements(exampleIdentifiers)


        return elements
    }

    private fun Element.createDomAddress(rootElement: Element): DomAddress{
        val domIdentifier = createDomIdentifier()
        val rootAddress = DomAddress(rootElement.createDomIdentifier(), null)
        val indexes = fillIndexList(rootElement)
        return DomAddress(domIdentifier, rootAddress, indexes)
    }

    private fun Element.createDomIdentifier(): DOMIdentifier{
        val attributes = mutableListOf<Pair<String, String?>>()
        if (!attributes().isEmpty) attributes.addAll(attributes().map { it.key to it.value })
        val identifierType: DomIdentifierType
        val value: String
        if (id().isNotEmpty()){
            identifierType = DomIdentifierType.ID
            value = id()
        } else if (className().isNotEmpty()){
            identifierType = DomIdentifierType.CLASS
            value = className()
        } else {
            identifierType = DomIdentifierType.TAG
            value = tagName()
        }
        return DOMIdentifier(value, identifierType, attributes)
    }

    private fun List<DOMIdentifier>.convertToIntermediate(): List<IntermediateElement> {
        try {
            val list = map {
                val element = document.getElement(it)
                IntermediateElement(element, it)
            }
            return list
        } catch (e: SeveralElementsException){
            val identifier = first() //todo unsafe
            return document.getElementsByDomIdentifier(identifier).map { IntermediateElement(it, identifier) }
        }
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

    private fun Element.getElementsByDomIdentifier(identifier: DOMIdentifier): Elements {
        return when(identifier.type){
            DomIdentifierType.CLASS -> getElementsByClass(identifier.value)
            DomIdentifierType.ID -> Elements(getElementById(identifier.value))
            DomIdentifierType.TAG -> getElementsByTag(identifier.value)
        }
    }

    private fun Document.getElement(identifier: DOMIdentifier): Element {
        val elements = getElementsByDomIdentifier(identifier)
        if (elements.isEmpty() || elements.first() == null) throw ElementNotFoundException()
        if (elements.size > 1){
            if (identifier.attributes != null){
                return getByAttr(identifier.attributes)
            } else throw SeveralElementsException()
        }
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