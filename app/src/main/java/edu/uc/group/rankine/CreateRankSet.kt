package edu.uc.group.rankine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.uc.group.rankine.ui.main.MainFragment

class CreateRankSet : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_rank_set)
        setSupportActionBar(findViewById(R.id.toolbar))

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .commitNow()
        }
    }
}