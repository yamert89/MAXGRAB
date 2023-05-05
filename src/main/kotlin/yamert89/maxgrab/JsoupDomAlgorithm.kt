package yamert89.maxgrab

import org.jsoup.nodes.Document

interface JsoupDomAlgorithm {
    fun findSourceHtmlElements(document: Document, inputIdentifiers: List<DOMIdentifier>): List<HtmlElement<*>>
    fun findTreeHtmlElements(document: Document, inputIdentifiers: List<DOMIdentifier>): List<HtmlElement<*>>

}