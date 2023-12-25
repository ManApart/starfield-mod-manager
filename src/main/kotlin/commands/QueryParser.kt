package commands

import Mod

typealias SearchExpression = (Mod) -> Boolean

fun thing(searchText: String): SearchExpression {
    //recursivly find innermost scope
    return {
        val inner = thing(searchText.split(" ").last())
        it.name.contains(searchText) && inner(it)
    }
}