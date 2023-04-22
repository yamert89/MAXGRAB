package yamert89.maxgrab

open class DomAddress(val identifier: DOMIdentifier<*>, val rootAddress: DomAddress? = null, val indexes: List<Int> = emptyList()) {

    override fun equals(other: Any?): Boolean {
        return if (other !is DomAddress) false
        else identifier == other.identifier
                && rootAddress == other.rootAddress
                && indexes == other.indexes
    }
}


