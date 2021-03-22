package edu.uc.group.rankine.utilities

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.annotation.Nullable
import androidx.core.view.children
import androidx.core.view.iterator
import edu.uc.group.rankine.R
import edu.uc.group.rankine.ui.createRank.CreateRankSet
import edu.uc.group.rankine.ui.main.MainActivity
import edu.uc.group.rankine.ui.main.MainFragment
import org.json.JSONArray
import org.json.JSONObject

/**
 * Utility that adds and removes fields dynamically and saves data from editTextViews to a sharedPreference
 */
class DynamicFieldUtil(activity: Activity) : Application() {
    private var _activity = activity
    val sharedPreferences = _activity.getSharedPreferences("onCreatePreferences", Context.MODE_PRIVATE)
    private val sharedEditor = sharedPreferences.edit()
    private val getAllViewChildren = GetAllViewChildren()
    private val nameEditText = _activity.findViewById<EditText>(R.id.name_edit_view)
    private val scrollContainer = _activity.findViewById<LinearLayout>(R.id.scroll_Container)
    private var attribute = ""
    private var jsonObject: JSONObject = JSONObject()

    /**
     * adds the layout dynamic_elements to the scrollContainer dynamically
     */
    fun addElements(view: View) {
        val inflater = _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.dynamic_elements, null)
        val parent: ViewGroup = scrollContainer as ViewGroup
        rowView.id = View.generateViewId()
        parent!!.addView(rowView)
    }

    /**
     * removes the layout dynamic_elements form the scrollContainer
     */
    fun removeElements(view: View) {
        scrollContainer!!.removeView(view.parent.parent as View)
    }

    /**
     * filters input from the editText views and saves it into an JSONObject
     * the JSONObject is then passed to the sharedPreference
     */
    fun create(view: View) {
        var jsonArray = JSONArray()
        val storeNameText = nameEditText.text.toString()
        val allViews: ArrayList<View> = getAllViewChildren.getAllChildren(scrollContainer!!)
        for (child: View in allViews) {
            if (child is EditText) run {
                if (child.tag is String) run {
                    var childEditViews = child as EditText
                    attribute = childEditViews.text.toString()
                    var help = child
                    var test = help.parent.parent as View
                    val allLayoutChildren = getAllViewChildren.getAllChildren(test)
                    for (variable in allLayoutChildren) {
                        if (variable is EditText) {
                            if (variable.tag == null) {
                                var childEditViews = variable.text.toString()
                                jsonArray.put(childEditViews)
                            }
                        }
                    }
                    jsonObject.put(attribute, jsonArray)
                    jsonArray = JSONArray(ArrayList<String>())
                }
            }
        }
        sharedEditor.putString("values", jsonObject.toString()).apply()
        sharedEditor.putString("name", storeNameText).apply()
        val intent = Intent(_activity, MainFragment::class.java)
    }
}