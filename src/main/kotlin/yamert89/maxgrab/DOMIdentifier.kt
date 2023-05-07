package yamert89.maxgrab

class DOMIdentifier(val value: String, val type: DomIdentifierType, val attributes: List<Pair<String, String?>>? = null) {
    override fun equals(other: Any?): Boolean {
        return other is DOMIdentifier
                && value == other.value
                && type == other.type
                && attributes == other.attributes
    }

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + (attributes?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "|identifier|:$value, $type, $attributes"
    }
}

enum class DomIdentifierType {
    CLASS,
    ID,
    TAG
}
