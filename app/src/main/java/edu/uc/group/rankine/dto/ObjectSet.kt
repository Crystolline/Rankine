package edu.uc.group.rankine.dto

import android.graphics.Bitmap
import com.google.firebase.firestore.Exclude
import org.json.JSONObject

/**
 * An unranked set of <elements>
 */
data class ObjectSet(
    var name: String = "",
    var localUri: String = "",
    var menu: Boolean = false
) {

    private var _elements: ArrayList<ElementObject> = ArrayList<ElementObject>()

    var elements: ArrayList<ElementObject>
        @Exclude get() {
            return _elements
        }
        set(value) {
            _elements = value
        }

    /**
     * Add element <e> to the ObjectSet's element list
     */
    fun addElement(e: ElementObject) {
        elements.add(e)


    }

    /**
     * Get elements
     */
    fun getAllElements(): ArrayList<ElementObject> {
        return elements
    }

    /**
     * Modify an element <inputObject> in the ObjectSet with the name and attributes of element <newObject>
     */
    fun modifyElements(inputObject: ElementObject, newObject: ElementObject) {
        elements.remove(inputObject)
        addElement(newObject)
    }

    fun setShowMenu(b: Boolean) {
        menu = b
    }

    fun isShowMenu(): Boolean {
        if (menu) {
            return true
        }
        return false

    }

}

