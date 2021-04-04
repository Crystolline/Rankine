package edu.uc.group.rankine.dto

import org.json.JSONObject

/**
 * An unranked set of <elements> alongside a list of <fields> to define attributes for individual elements
 */


data class ObjectSet(
        var objectSet: String = "",
        var localUri: String = ""
) {


    fun getUserJSONData(data: String, photo: String) {
        objectSet = data
        localUri = photo
    }


}


