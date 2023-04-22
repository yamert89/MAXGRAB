package yamert89.maxgrab

class HtmlElement<T>(val id: Long, val value: T, val address: DomAddress?) {
    //private val children: MutableMap<String, HtmlElement<*,*>> = mutableMapOf()

    /*fun addChild(child: HtmlElement<*,*>){
        children[child.address!!.indexes.joinToString()] = child
    }*/

    /*fun getChildren(): Map<String, HtmlElement<*, *>>{
        return children
    }*/

    override fun equals(other: Any?): Boolean {
        return if (other !is HtmlElement<*>) false
        else if (this === other /*|| id == other.id*/) true
        else {
            value == other.value && this.address == other.address
        }
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + (address?.hashCode() ?: 0)
        return result
    }
}
