package yamert89.maxgrab

import org.jsoup.Jsoup
import java.io.File


class JsoupPageParser(private val domAlgorithm: JsoupDomAlgorithm): PageParser{
    override fun parseSourceElements(url: String): List<HtmlElement<*>> {
        return emptyList()
    }

    override fun parseSourceElements(file: File, inputIdentifiers: List<DOMIdentifier<*>>): List<HtmlElement<*>> {
        val document = Jsoup.parse(file)
        return domAlgorithm.findSourceHtmlElements(document, inputIdentifiers)
    }

    override fun parseTree(file: File, inputIdentifiers: List<DOMIdentifier<String>>): List<HtmlElement<*>> {
        val document = Jsoup.parse(file)
        return domAlgorithm.findTreeHtmlElements(document, inputIdentifiers)
    }
}
