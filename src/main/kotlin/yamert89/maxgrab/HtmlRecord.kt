package yamert89.maxgrab

class HtmlRecord(val id: Long, val elements: List<HtmlElement<*, *>>) {
    override fun equals(other: Any?): Boolean {
        return if (other !is HtmlRecord) false
        else if (elements.size != other.elements.size) false
        else run {
            for (i in elements.indices){
                if (elements[i] != other.elements[i]) return false
            }
            return true
        };
    }
}
