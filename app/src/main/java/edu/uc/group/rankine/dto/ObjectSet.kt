package edu.uc.group.rankine.dto

data class ObjectSet(var elements:ArrayList<ElementObject> = ArrayList<ElementObject>(), var fields:ArrayList<FieldObject> = ArrayList<FieldObject>(), var ordered:Boolean = false) {

    fun addElements(e: ElementObject) {
        if (!elements.add(e)) {
            //Logic for if item already exists within this set
        } else {
        }
    }

    fun addFields(f: FieldObject) {
        if (!fields.add(f)) {
            //Logic for if item already exists within this set
        } else {
        }
    }

    fun modifyElements(elements: Set<*>?) {
        //Bring up a list of each element
        //Select whether the user would like to delete or edit the elements in a set
    }

    fun modifyFields(fields: Set<*>?) {
        //Bring up a list of each field
        //Select whether the user would like to delete or edit the fields in a set
    }

}