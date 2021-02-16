package edu.uc.group.rankine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import edu.uc.group.rankine.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.toolbar))

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.search_icon) {
            Toast.makeText( applicationContext, "Message", Toast.LENGTH_SHORT).show()
            return true
        }

        else if(item.itemId == R.id.search_menu_item01) {
            Toast.makeText( applicationContext, "Menu", Toast.LENGTH_SHORT).show()
            return true
        }else{ return super.onOptionsItemSelected(item) }
    }


    
}