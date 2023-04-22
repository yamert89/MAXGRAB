package yamert89.maxgrab

class DOMIdentifier<T>(val value: T, val type: DomIdentifierType) {
    override fun equals(other: Any?): Boolean {
        return other is DOMIdentifier<*>
                && value == other.value
                && type == other.type
    }
}

enum class DomIdentifierType {
    CLASS
}
