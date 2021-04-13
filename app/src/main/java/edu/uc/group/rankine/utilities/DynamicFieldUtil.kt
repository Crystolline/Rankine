package edu.uc.group.rankine.utilities

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.view.iterator
import edu.uc.group.rankine.R
import org.json.JSONArray
import org.json.JSONObject

/**
 * Utility that adds and removes fields dynamically and saves data from editTextViews to a sharedPreference
 */
class DynamicFieldUtil(activity: Activity) : Application() {
    private var _activity = activity
    private val getAllViewChildren = GetAllViewChildren()
    private val nameEditText = _activity.findViewById<EditText>(R.id.name_edit_view)
    private val scrollContainer = _activity.findViewById<LinearLayout>(R.id.scroll_Container)
    private var attribute = ""
    private var counter = 0
    var jsonObject: JSONObject = JSONObject()
    var jsonArray: JSONArray = JSONArray()
    var jsonArrayHolder = JSONArray()
    var jsonObjectHolder = JSONObject()


    /**
     * adds the layout dynamic_elements to the scrollContainer dynamically
     */
    fun addElements() {
        val inflater = _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.dynamic_elements, null)
        val parent: ViewGroup = scrollContainer as ViewGroup
        rowView.id = View.generateViewId()
        parent.addView(rowView)
    }

    /**
     * removes the layout dynamic_elements form the scrollContainer
     */
    fun removeElements(view: View) {
        val scrollContainer = _activity.findViewById<LinearLayout>(R.id.scroll_Container)
        val test = view.parent.parent
        scrollContainer.removeView(test as View)
    }

    /**
     * filters input from the editText views and saves it into an JSONObject
     * the JSONObject is then passed to the sharedPreference
     */
    fun create(): String {
        val allViews: ArrayList<View> = getAllViewChildren.getAllChildren(scrollContainer!!)
        var jsonArray = JSONArray()
        val storeNameText = nameEditText.text.toString()
        for (child: View in allViews) {
            if (child is EditText) run {
                attribute = child.text.toString()

                jsonObject = JSONObject()
                val attributeJson = jsonObject.put("Attribute$counter", attribute)
                jsonArray.put(attributeJson)
                counter++

            }
        }
        jsonObject = JSONObject()
        jsonObject.put("Name", storeNameText)
        jsonArray.put(jsonObject)
        jsonObject = JSONObject()
        jsonObject.put("WholeSet", jsonArray)
        return jsonObject.toString()

    }

    fun userFilter(view: ArrayList<View>): Boolean {
        val storeNameText = nameEditText.text
        for (child: View in view) {
            if (child is EditText) {
                if (child.text.toString() == "" || storeNameText.toString() == "") {
                    return false
                }
            }
        }
        return true
    }


}