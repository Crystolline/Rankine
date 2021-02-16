package edu.uc.group.rankine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class CreateRankSet : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_rank_set)
        setSupportActionBar(findViewById(R.id.toolbar)) // set toolbar

        assert(supportActionBar != null) //implement back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


    }

    override fun onSupportNavigateUp(): Boolean { // back btn functionality
        finish()
        return true
    }
}