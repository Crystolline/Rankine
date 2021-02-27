package edu.uc.group.rankine.utilities

import android.view.View
import android.view.ViewGroup

class GetAllViewChildren {

    fun getAllChildren(v: View): java.util.ArrayList<View> {
        if (v !is ViewGroup) {
            val viewArrayList = java.util.ArrayList<View>()
            viewArrayList.add(v)
            return viewArrayList
        }
        val result = java.util.ArrayList<View>()
        for (i in 0 until v.childCount) {
            val child = v.getChildAt(i)
            val viewArrayList = java.util.ArrayList<View>()
            viewArrayList.add(v)
            viewArrayList.addAll(getAllChildren(child))
            result.addAll(viewArrayList)
        }
        return result
    }
}