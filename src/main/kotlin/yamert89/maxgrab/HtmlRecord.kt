package yamert89.maxgrab

class HtmlRecord(val id: Long, val elements: List<HtmlElement<*>>) {
    override fun equals(other: Any?): Boolean {
        return if (other !is HtmlRecord) false
        else if (elements.size != other.elements.size) false
        else run {
            for (i in elements.indices){
                if (elements[i] != other.elements[i]) return false
            }
            return true
        }
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + elements.hashCode()
        return result
    }

    override fun toString(): String {
        return "record: id = $id, elements = [\n{${elements.joinToString("},\n{")}}\n]\n"
    }
}
