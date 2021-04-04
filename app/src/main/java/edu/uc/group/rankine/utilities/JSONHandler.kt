package edu.uc.group.rankine.utilities

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import edu.uc.group.rankine.ui.main.MainActivity
import java.util.jar.Attributes

class JSONHandler {
    private val mainActivity = MainActivity()


    companion object {

        var Name = ""
        var ObjectSetsArrayList: ArrayList<LinkedTreeMap<*, *>>? = null
        private var counter = 0
        private fun jsonObjectSetHandler(inputString: String): Root? {
            val gson = Gson()
            val newString = inputString.replace("\"", "'")
            return gson.fromJson(newString, Root::class.java)
        }

        fun pullData(inputString: String) {
            val dirtyData = jsonObjectSetHandler(inputString)?.wholeSet
            val getNameTree = dirtyData?.get(0) as LinkedTreeMap<*, *>
            var name = getNameTree.values.toString()
            name = name.replace("[", "")
            name = name.replace("]", "")
            Name = name

            val getObjectSetArrayList = dirtyData[1] as ArrayList<Object>
            for (counter in 0 until getObjectSetArrayList.size) {
                val set = getObjectSetArrayList[counter] as ArrayList<LinkedTreeMap<*, *>>?
                ObjectSetsArrayList = set

            }

        }
    }

    class Root {
        val wholeSet: List<Object>? = null
    }


}