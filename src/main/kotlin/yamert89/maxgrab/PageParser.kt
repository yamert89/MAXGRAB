package yamert89.maxgrab

import java.io.File

interface PageParser {
    fun parseSourceElements(url: String): List<HtmlElement<*>>
    fun parseSourceElements(file: File, inputIdentifiers: List<DOMIdentifier<*>>): List<HtmlElement<*>>
    fun parseTree(file: File, inputIdentifiers: List<DOMIdentifier<String>>): List<HtmlElement<*>>


}