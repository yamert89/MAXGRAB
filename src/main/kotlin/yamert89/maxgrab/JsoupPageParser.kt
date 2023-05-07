package yamert89.maxgrab

import org.jsoup.Jsoup
import java.io.File


class JsoupPageParser: PageParser{
    override fun parseSourceElements(url: String): List<HtmlElement<*>> {
        return emptyList()
    }

    override fun parseSourceElements(file: File, inputIdentifiers: List<DOMIdentifier>): List<HtmlElement<*>> {
        val document = Jsoup.parse(file)
        val domAlgorithm: JsoupDomAlgorithm = JsoupDomAlgorithmImpl(document)
        return domAlgorithm.findSourceHtmlElements(inputIdentifiers)
    }

    override fun parseTree(file: File, inputIdentifiers: List<DOMIdentifier>, treeAlgorithm: TreeAlgorithm): List<HtmlElement<*>> {
        val document = Jsoup.parse(file)
        val domAlgorithm: JsoupDomAlgorithm = JsoupDomAlgorithmImpl(document)
        return domAlgorithm.findTreeHtmlElements(inputIdentifiers, treeAlgorithm)
    }
}
