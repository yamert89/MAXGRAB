package yamert89.maxgrab

import org.jsoup.nodes.Element

class IntermediateElement(val element: Element, val identifier: DOMIdentifier, val indexes: MutableList<Int> = mutableListOf()) {
}