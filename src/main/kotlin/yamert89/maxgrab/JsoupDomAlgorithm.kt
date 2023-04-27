package yamert89.maxgrab

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

interface JsoupDomAlgorithm {
    fun getHtmlElements(document: Document, inputIdentifiers: List<DOMIdentifier<*>>): List<HtmlElement<*>>

}