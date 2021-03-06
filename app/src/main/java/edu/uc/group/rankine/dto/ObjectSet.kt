package edu.uc.group.rankine.dto

/**
 * An unranked set of <elements> alongside a list of <fields> to define attributes for individual elements
 */
data class ObjectSet(var elements: ArrayList<ElementObject> = ArrayList(), var fields: ArrayList<FieldObject> = ArrayList())
{

    /**
     * Add element <e> to the ObjectSet's element list
     */
    fun addElement(e: ElementObject)
    {
        elements.add(e)

    }

    /**
     * Add field <f> to the ObjectSet's fields list
     */
    fun addField(f: FieldObject)
    {
        fields.add(f)
    }

    /**
     * Modify an element <inputObject> in the ObjectSet with the name and attributes of element <newObject>
     */
    fun modifyElements(inputObject: ElementObject, newObject : ElementObject)
    {
        //Eventually should bring up a list of each field
        //Select whether the user would like to delete or edit the fields in a set
        elements.remove(inputObject)
        addElement(newObject)
    }

    /**
     * Modify a field <inputObject> in the ObjectSet with the name and datatype of field <newObject>
     */
    fun modifyFields(inputObject: FieldObject, newObject : FieldObject)
    {
        //Eventually should bring up a list of each field
        //Select whether the user would like to delete or edit the fields in a set
        fields.remove(inputObject)
        addField(newObject)
    }

}


