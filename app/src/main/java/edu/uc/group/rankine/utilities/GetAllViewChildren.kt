package edu.uc.group.rankine.utilities

import android.view.View
import android.view.ViewGroup

/**
 * gets all children of a passed view or viewGroup
 */
class GetAllViewChildren {

    fun getAllChildren(v: View): ArrayList<View> {
        if (v !is ViewGroup) {
            val viewArrayList = ArrayList<View>()
            viewArrayList.add(v)
            return viewArrayList
        }
        val result = ArrayList<View>()
        for (i in 0 until v.childCount) {
            val child = v.getChildAt(i)
            val viewArrayList = ArrayList<View>()
            viewArrayList.add(v)
            viewArrayList.addAll(getAllChildren(child))
            result.addAll(viewArrayList)
        }
        return result
    }
}