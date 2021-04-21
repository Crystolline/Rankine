package edu.uc.group.rankine.dto

import com.google.firebase.firestore.Exclude
import edu.uc.group.rankine.ui.main.MainFragment

/**
 * A set of unranked elements
 * @property id the Firestore id of the ObjectSet
 * @property name the name of the ObjectSet
 * @property localUri the local uri of the ObjectSet's image
 * @property elements the list of [ElementObject]s the ObjectSet stores
 * @property menu whether the menu on the [MainFragment] for this ObjectSet is showing
 */
data class ObjectSet(
    var id: String = "",
    var name: String = "",
    var localUri: String = ""
) {

    private var _elements: ArrayList<ElementObject> = ArrayList()
    private var _menu = false

    var elements: ArrayList<ElementObject>
        @Exclude get() {
            return _elements
        }
        set(value) {
            _elements = value
        }

    var menu: Boolean
        @Exclude get() {
            return _menu
        }
        set(value) {
            _menu = value
        }

    /**
     * Add element [e] to the ObjectSet's element list
     */
    fun addElement(e: ElementObject) {
        elements.add(e)
    }

    /**
     * Modify an element [inputObject] in the ObjectSet with the name and attributes of element [newObject]
     */
    fun modifyElements(inputObject: ElementObject, newObject: ElementObject) {
        elements.remove(inputObject)
        addElement(newObject)
    }

}

