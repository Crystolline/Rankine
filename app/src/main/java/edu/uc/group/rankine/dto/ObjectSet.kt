package edu.uc.group.rankine.dto

import org.json.JSONObject

/**
 * An unranked set of <elements>
 */
data class ObjectSet(var elements: ArrayList<ElementObject> = ArrayList(),
                     var objectSet: String = "",
                     var localUri: String = "") {
    /**
     * Add element <e> to the ObjectSet's element list
     */
    fun addElement(e: ElementObject) {
        elements.add(e)


    }

    /**
     * Get elements
     */
    fun getAllElements() : ArrayList<ElementObject>
    {
        return elements
    }

    /**
     * Modify an element <inputObject> in the ObjectSet with the name and attributes of element <newObject>
     */
    fun modifyElements(inputObject: ElementObject, newObject: ElementObject) {
        elements.remove(inputObject)
        addElement(newObject)


    }

    fun getUserJSONData(data: String, photo: String) {
        objectSet = data
        localUri = photo
    }
}

