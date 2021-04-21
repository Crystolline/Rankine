package edu.uc.group.rankine.ui.main

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import edu.uc.group.rankine.R
import edu.uc.group.rankine.ui.ranking.RankSetFragment
import edu.uc.group.rankine.ui.ranking.RankSetViewFragment
import edu.uc.group.rankine.ui.ranking.ViewSelectedRankSetFragment
import java.io.File
import java.io.FileOutputStream
import java.util.*


@Suppress("unused")
class MainActivity : AppCompatActivity() {

    private lateinit var vm: MainViewModel
    private lateinit var vmFactory: MainViewModelFactory

    private lateinit var mainFragment: MainFragment
    private lateinit var rankSetFragment: RankSetFragment
    private lateinit var createRankSetFragment: CreateRankSetFragment
    private lateinit var rankedSetViewFragment: RankSetViewFragment
    private lateinit var viewSelectedRankSetFragment: ViewSelectedRankSetFragment
    private var activeFragment: Fragment = Fragment()

    private val imageCode: Int = 204
    private var imageUri: Uri? = null

    @Suppress("PrivatePropertyName")
    private val AUTH_REQUEST_CODE = 2002
    private var user: FirebaseUser? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.toolbar))

        //instantiate fragments
        mainFragment = MainFragment.newInstance()
        rankSetFragment = RankSetFragment.newInstance()
        createRankSetFragment = CreateRankSetFragment.newInstance()
        rankedSetViewFragment = RankSetViewFragment.newInstance()
        viewSelectedRankSetFragment = ViewSelectedRankSetFragment.newInstance()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, mainFragment)
                .commitNow()
            activeFragment = mainFragment
        }

        vmFactory = MainViewModelFactory()
        vm = ViewModelProvider(this, vmFactory)
            .get(MainViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == imageCode) run {
            val imageView: ImageView = findViewById(R.id.set_image)
            imageView.clipToOutline = true
            imageUri = data?.data
            val uniqueString: String = UUID.randomUUID().toString()
            val fileName =
                File(applicationContext.filesDir.absolutePath + File.separator + "$uniqueString.png")

            //Thing to use for firebase auth
            if (requestCode == AUTH_REQUEST_CODE) {
                user = FirebaseAuth.getInstance().currentUser
            }

            fileName.createNewFile()
            val outputStream = FileOutputStream(fileName)
            vm.getImageUriString(fileName.absolutePath)
            val source = ImageDecoder.createSource(contentResolver, imageUri!!)
            val bitmap = ImageDecoder.decodeBitmap(source)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            imageView.setImageBitmap(bitmap)
        }
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

        return if (item.itemId == R.id.search_menu_item01) {
            Toast.makeText(applicationContext, "Menu", Toast.LENGTH_SHORT).show()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    /**
     *  Creates a new intent that allows the user to pick a image to represent a RankSet.
     */
    fun View.onImageAdd() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.flags =
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION and Intent.FLAG_GRANT_READ_URI_PERMISSION and Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        intent.type = "image/*"
        startActivityForResult(intent, imageCode)
    }

    /**
     * Changes the active fragment to the [MainFragment]
     */
    internal fun moveToMain() {
        if (activeFragment != mainFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, mainFragment)
                .commitNow()
            activeFragment = mainFragment
        }
    }

    /**
     * Changes the active fragment to the [rankSetFragment]
     */
    internal fun moveToRankSet() {
        if (activeFragment != rankSetFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, rankSetFragment)
                .commitNow()
            rankSetFragment.updateRankSetView()
            activeFragment = rankSetFragment
        }
    }

    /**
     * Changes the active fragment to the [CreateRankSetFragment]
     */
    internal fun moveToCreateRankSet() {
        if (activeFragment != createRankSetFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, createRankSetFragment)
                .commitNow()
            createRankSetFragment.updateCreateRankSetView()
            activeFragment = createRankSetFragment
        }
    }

    /**
     * Changes the active fragment to the [RankSetViewFragment]
     */
    internal fun moveToRankedSetViewFragment() {
        rankedSetViewFragment = RankSetViewFragment.newInstance()
        if (activeFragment != rankedSetViewFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, rankedSetViewFragment)
                .commitNow()
            activeFragment = rankedSetViewFragment
        }
    }

    /**
     * Changes the active fragment to the [ViewSelectedRankSetFragment]
     */
    internal fun moveToViewSelectedRankSetFragment() {
        if (activeFragment != viewSelectedRankSetFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, viewSelectedRankSetFragment)
                .commitNow()
            viewSelectedRankSetFragment.updateSelectedRankSetView()
            activeFragment = viewSelectedRankSetFragment
        }
    }

    //Authentication stuff, needs to be made into the launch screen and only allow to the main app after successful authentication
    private fun logon() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                .build(), AUTH_REQUEST_CODE
        )
    }
}