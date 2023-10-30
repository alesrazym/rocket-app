package cz.quanti.razym.rocketapp.system

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cz.quanti.razym.rocketapp.R

class RocketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rocket)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, RocketListFragment.newInstance())
                .commitNow()
        }
    }
}