package edu.uc.group.rankine.ui.createRank

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import edu.uc.group.rankine.R

class CreateRankSet : AppCompatActivity() {



    private var scrollContainer : LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_rank_set)
        setSupportActionBar(findViewById(R.id.toolbar)) // set toolbar
        val createFieldBtn:androidx.appcompat.widget.AppCompatButton = findViewById(R.id.add_new_field_btn)
        scrollContainer = findViewById(R.id.scroll_Container)
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
        val inflater=
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView : View = inflater.inflate(R.layout.dynamic_fields, null)
        scrollContainer!!.addView(rowView, scrollContainer!!.childCount -1)
    }
}