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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.uc.group.rankine.R
import edu.uc.group.rankine.ui.ranking.RankSetFragment
import java.io.File
import java.io.FileOutputStream
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var vm: MainViewModel
    private lateinit var vmFactory: MainViewModelFactory
    private lateinit var mainFragment: MainFragment
    private lateinit var rankSetFragment: RankSetFragment
    private lateinit var createRankSetFragment: CreateRankSetFragment
    private lateinit var editRankFragment: EditRankFragment
    private var activeFragment: Fragment = Fragment()
    private val imageCode: Int = 204
    var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.toolbar))
        mainFragment = MainFragment.newInstance()
        rankSetFragment = RankSetFragment.newInstance()
        createRankSetFragment = CreateRankSetFragment.newInstance()
        editRankFragment = EditRankFragment.newInstance()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, mainFragment)
                    .commitNow()
            activeFragment = mainFragment
        }

        vmFactory = MainViewModelFactory(this)
        vm = ViewModelProvider(this, vmFactory)
                .get(MainViewModel::class.java)

        vm.objectSets.observe(this, Observer { objectSet ->
            var observeThis = objectSet
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == imageCode) run {
            val imageView: ImageView = findViewById(R.id.create_rank_fragment_image)
            imageView.clipToOutline = true
            imageUri = data?.data
            val uniqueString: String = UUID.randomUUID().toString()
            val fileName = File(applicationContext.filesDir.absolutePath + File.separator + "$uniqueString.png")
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
     *  Creates a new intent that allows the user to pick a image to represent a RankSet.
     */
    fun onImageAdd(view: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.flags =
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION and Intent.FLAG_GRANT_READ_URI_PERMISSION and Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        intent.type = "image/*"
        startActivityForResult(intent, imageCode)
    }

    internal fun moveToMain() {
        mainFragment = MainFragment.newInstance()
        if (activeFragment != mainFragment) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, mainFragment)
                    .commitNow()
            activeFragment = mainFragment
        }
    }

    internal fun moveToRankSet() {
        rankSetFragment = RankSetFragment.newInstance()
        if (activeFragment != rankSetFragment) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, rankSetFragment)
                    .commitNow()
            activeFragment = rankSetFragment
        }
    }

    internal fun moveToCreateRankSet() {
        createRankSetFragment = CreateRankSetFragment.newInstance()
        if (activeFragment != createRankSetFragment) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, createRankSetFragment)
                    .commitNow()
            activeFragment = createRankSetFragment
        }
    }

    internal fun moveToEditFragment() {
        editRankFragment = EditRankFragment.newInstance()
        if (activeFragment != editRankFragment) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, editRankFragment)
                    .commitNow()
            activeFragment = editRankFragment
        }
    }
}