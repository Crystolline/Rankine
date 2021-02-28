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

class DynamicFieldUtil(activity: Activity): Application() {
    var context = activity
    private var _activity = activity

    private val jsonHandler = JsonHandler()
    private val getAllViewChildren = GetAllViewChildren()
    private val nameEditText = _activity.findViewById<EditText>(R.id.name_edit_view)
    private val  scrollContainer = _activity.findViewById<LinearLayout>(R.id.scroll_Container)


    fun addElements(){


        val inflater= _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView : View = inflater.inflate(R.layout.dynamic_elements, null)
        val parent:ViewGroup = scrollContainer as ViewGroup
        rowView.id = View.generateViewId()
        parent!!.addView(rowView)

        for (i in parent) {
            i.id = View.generateViewId()
            var id = i.id
        }


    }

    fun removeElements(view: View){
        scrollContainer!!.removeView(view.parent.parent as View)
    }

    fun addFields(id: Int){
        val inflater= _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val btn: ImageButton = _activity.findViewById(id)
        val parent = btn.parent.parent as ViewGroup
        val row : View = inflater.inflate(R.layout.dynamic_fields, null)
        parent!!.addView(row)
    }

    fun removeFields(view: View, id:Int){
        val btn: ImageButton = _activity.findViewById(id)
        val parent = btn.parent.parent as ViewGroup
        parent.removeView(view.parent as View)
    }

    fun create(){

        val storeNameText = nameEditText.text.toString()
        val test = jsonHandler.createJsonFile(storeNameText, _activity)


        val allViews: ArrayList<View> = getAllViewChildren.getAllChildren(scrollContainer!!)
        for (child: View in allViews){
            if(child is EditText) {
                run {
                    var childEditViews = child as EditText
                    var storeText = childEditViews.text.toString()
                    jsonHandler.saveJsonFile(test , storeText)

                }
            }
        }
    }
}