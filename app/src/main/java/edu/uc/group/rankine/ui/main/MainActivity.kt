package edu.uc.group.rankine.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.children
import androidx.core.view.iterator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import edu.uc.group.rankine.R
import edu.uc.group.rankine.ui.createRank.CreateRankSet
import edu.uc.group.rankine.ui.createRank.CreateRankSetViewModel
import edu.uc.group.rankine.ui.createRank.CreateRankSetViewModelFactory
import edu.uc.group.rankine.utilities.JSONHandler
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var viewModelCreateRank: CreateRankSetViewModel
    private lateinit var viewModelFactoryCreateRank: CreateRankSetViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.toolbar))


        /**
         * on floating action click open CreateRankSet
         */
        val btnOpenActivity: FloatingActionButton = findViewById(R.id.createRankBtn)
        btnOpenActivity.setOnClickListener {
            val intent = Intent(this, CreateRankSet::class.java)
            startActivity(intent)
        }

        viewModelFactoryCreateRank = CreateRankSetViewModelFactory(this)
        viewModelCreateRank = ViewModelProvider(this, viewModelFactoryCreateRank)
                .get(CreateRankSetViewModel::class.java)

        viewModelCreateRank.objectSetLiveData.observe(this, Observer { objectSet ->
            var test = objectSet
        })


    }

    override fun onStart() {
        super.onStart()
        addView()
    }

    /**
     * Creates toolbar
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.search_icon)
        val searchView = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery(query, false)
                searchItem.collapseActionView()
                Toast.makeText(this@MainActivity, "looking for $query", Toast.LENGTH_LONG).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    /**
     * Logic if option is selected within the toolbar
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.search_menu_item01) {
            Toast.makeText(applicationContext, "Menu", Toast.LENGTH_SHORT).show()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    /**
     * unimplemented
     */
    fun loadView() {

    }

    /**
     * unimplemented
     */
    private fun addView() {
        removeView()
        viewModelCreateRank.objectSetLiveData.observe(this, Observer { objectSet ->
            var counter = 0
            if (viewModelCreateRank.objectSetLiveData == null) {
                return@Observer
            } else {
                for (counter in 0 until objectSet.size) {

                    val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val row: View = inflater.inflate(R.layout.dynamic_main_view, null)
                    var id = row.id
                    id = View.generateViewId()
                    val parent = findViewById<LinearLayout>(R.id.data_container)
                    parent.addView(row)
                    val currentObjectSet = objectSet[counter]
                    val parentAtZero = row as LinearLayout
                    val parentAtOne = parentAtZero.getChildAt(0) as LinearLayout
                    val childAtZero = parentAtOne.getChildAt(1) as View
                    if (childAtZero is TextView) {

                        val getName = JSONHandler.jsonNameHandler(currentObjectSet.objectSet)?.WholeSet
                        val getObject = JSONHandler.jsonObjectSetHandler(currentObjectSet.objectSet)?.ObjectSets
                        childAtZero.text = getName?.get(0)?.Name
                    }
                }
            }
        })

    }

    private fun removeView() {
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row: View = inflater.inflate(R.layout.dynamic_main_view, null)
        val parent = row as LinearLayout
        parent.removeView(parent)
    }


}