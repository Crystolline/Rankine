package edu.uc.group.rankine.dto

/**
 * An Element with a name that can be ranked and assigned attributes
 * @property element the name of the element
 * @property id the id of the element for Firestore
 */
data class ElementObject(var element: String = "", var id: String = "") {
    override fun toString(): String {
        return element
    }
}