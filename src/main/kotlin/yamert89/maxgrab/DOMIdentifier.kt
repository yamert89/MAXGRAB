package yamert89.maxgrab

class DOMIdentifier<T>(val value: T, val type: DomIdentifierType) {
    override fun equals(other: Any?): Boolean {
        return other is DOMIdentifier<*>
                && value == other.value
                && type == other.type
    }

    override fun hashCode(): Int {
        var result = value?.hashCode() ?: 0
        result = 31 * result + type.hashCode()
        return result
    }
}

enum class DomIdentifierType {
    CLASS
}
