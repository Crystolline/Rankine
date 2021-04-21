package edu.uc.group.rankine.dto

import com.google.firebase.firestore.Exclude

/**
 * An unranked set of <elements>
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
     * Add element <e> to the ObjectSet's element list
     */
    fun addElement(e: ElementObject) {
        elements.add(e)
    }

    /**
     * Modify an element <inputObject> in the ObjectSet with the name and attributes of element <newObject>
     */
    fun modifyElements(inputObject: ElementObject, newObject: ElementObject) {
        elements.remove(inputObject)
        addElement(newObject)
    }

}

