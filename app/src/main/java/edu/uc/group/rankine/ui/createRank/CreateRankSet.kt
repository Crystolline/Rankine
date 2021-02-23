package edu.uc.group.rankine.ui.createRank

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import edu.uc.group.rankine.R

class CreateRankSet : AppCompatActivity() {



    private var scrollContainer : LinearLayout? = null
    private var testContainer : LinearLayout? = null
    private var dynamicList: ArrayList<EditText> = ArrayList<EditText>()
    private var i: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_rank_set)
        setSupportActionBar(findViewById(R.id.toolbar)) // set toolbar
        val createFieldBtn:androidx.appcompat.widget.AppCompatButton = findViewById(R.id.add_new_field_btn)
        scrollContainer = findViewById(R.id.scroll_Container)
        testContainer = findViewById(R.id.test_Container)
        assert(supportActionBar != null) //implement back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean { // back btn functionality
        finish()
        return true
    }

    fun onDelete(view: View){
        scrollContainer!!.removeView(view.parent as View)
    }

    fun onAdd(view: View){
        val inflater= getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView : View = inflater.inflate(R.layout.dynamic_fields, null)
        scrollContainer!!.addView(rowView, scrollContainer!!.childCount)
    }

    fun onCreate(view: View) {

        scrollContainer = findViewById(R.id.scroll_Container)

        val allViews: ArrayList<View> = getAllChildren(scrollContainer!!)
        for (child: View in allViews){
            if(child is EditText) {
                run {
                    var childEditViews = child as EditText
                    var storeText = childEditViews.text.toString()
                }
            }
        }
    }

    private fun getAllChildren(v: View): java.util.ArrayList<View> {
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