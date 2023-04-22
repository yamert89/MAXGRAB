package yamert89.maxgrab

import java.io.File

interface PageParser {
    fun parse(url: String): List<HtmlRecord>
    fun parse(file: File, inputIdentifiers: List<DOMIdentifier<*>>): List<HtmlRecord>
}