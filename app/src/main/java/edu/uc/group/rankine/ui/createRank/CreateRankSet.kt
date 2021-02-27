package edu.uc.group.rankine.ui.createRank


import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import edu.uc.group.rankine.R
import edu.uc.group.rankine.ui.main.SetCreationViewModel
import edu.uc.group.rankine.ui.main.SetCreationViewModelFactory
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class CreateRankSet : AppCompatActivity() {



    private var scrollContainer : LinearLayout? = null
    private lateinit var viewModel: SetCreationViewModel
    private lateinit var viewModelFactory: SetCreationViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_rank_set)
        setSupportActionBar(findViewById(R.id.toolbar)) // set toolbar
        scrollContainer = findViewById(R.id.scroll_Container)
        assert(supportActionBar != null) //implement back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        viewModelFactory = SetCreationViewModelFactory(this)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SetCreationViewModel::class.java)

    }

    override fun onSupportNavigateUp(): Boolean { // back btn functionality
        finish()
        return true
    }

    fun onDelete(view: View){
        viewModel.removeElements(view)

    }

    fun onAdd(view: View){
        viewModel.addElements()
    }

    fun onCreate(view: View) {
        viewModel.create()
    }
}