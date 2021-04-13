package edu.uc.group.rankine.ui.main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.uc.group.rankine.R
import edu.uc.group.rankine.ui.ranking.RankSetFragment
import edu.uc.group.rankine.utilities.JSONHandler


class MainActivity : AppCompatActivity() {

    private lateinit var vm: MainViewModel
    private lateinit var vmFactory: MainViewModelFactory
    private lateinit var mainFragment: MainFragment
    private lateinit var rankSetFragment: RankSetFragment
    private lateinit var createRankSetFragment: CreateRankSetFragment
    lateinit var activeFragment: Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.toolbar))
        mainFragment = MainFragment.newInstance()
        rankSetFragment = RankSetFragment.newInstance()
        createRankSetFragment = CreateRankSetFragment.newInstance()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, mainFragment)
                    .commitNow()
            activeFragment = mainFragment
        }

        vmFactory = MainViewModelFactory(this)
        vm = ViewModelProvider(this, vmFactory)
                .get(MainViewModel::class.java)

        vm.objectSetLiveData.observe(this, Observer { objectSet ->
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
    private fun addView() {
        removeView()
        vm.objectSetLiveData.observe(this, Observer { objectSet ->
            var counter = 0
            if (vm.objectSetLiveData == null) {
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

    fun onDeleteElements(view: View) {
        vm.removeElements(view)

    }

    internal fun moveToMain() {
        if (activeFragment != mainFragment) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, mainFragment)
                    .commitNow()
            activeFragment = mainFragment
        }
    }

    internal fun moveToRankSet() {
        if (activeFragment != rankSetFragment) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, rankSetFragment)
                    .commitNow()
            activeFragment = rankSetFragment
        }
    }

    internal fun moveToCreateRankSet() {
        if (activeFragment != createRankSetFragment) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, createRankSetFragment)
                    .commitNow()
            activeFragment = createRankSetFragment
        }
    }
}