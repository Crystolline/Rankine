package edu.uc.group.rankine.dto

/**
 * An Element with a <name> that can be ranked and assigned attributes
 */
data class ElementObject(var element: String = "") {
    override fun toString(): String {
        return element
    }
}