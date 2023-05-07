package yamert89.maxgrab

import org.jsoup.nodes.Document

interface JsoupDomAlgorithm {
    fun findSourceHtmlElements(inputIdentifiers: List<DOMIdentifier>): List<HtmlElement<*>>
    fun findTreeHtmlElements(exampleIdentifiers: List<DOMIdentifier>, treeAlgorithm: TreeAlgorithm): List<HtmlElement<*>>

}