package edu.uc.group.rankine.utilities

import com.google.gson.Gson

class JSONHandler {
    companion object {
        fun jsonNameHandler(inputString: String): WholeSet? {
            val gson = Gson()
            val newString = inputString.replace("\"", "'")
            return gson.fromJson(newString, WholeSet::class.java)

        }

        fun jsonObjectSetHandler(inputString: String): Object? {
            val gson = Gson()
            val newString = inputString.replace("\"", "'")
            return gson.fromJson(newString, Object::class.java)
        }
    }

    class Name {
        val Name: String = ""
    }

    class WholeSet {
        val WholeSet: ArrayList<Name>? = null
    }

    class Object {
        val ObjectSets: ArrayList<Element>? = null
    }

    class Element {
        val Element: String = ""
    }


}