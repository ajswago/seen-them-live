package com.swago.seenthemlive

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.swago.seenthemlive.ui.common.BaseActivity
import com.swago.seenthemlive.ui.playlist.CreatePlaylistActivity
import com.swago.seenthemlive.ui.search.SearchActivity

class ViewConcertsActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var mUser: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_concerts)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val userId = intent.getStringExtra(INTENT_USER)
        mUser = userId

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_list, R.id.nav_profile, R.id.nav_artist, R.id.nav_map, R.id.nav_friends
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.view_concerts, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                val intent = CreatePlaylistActivity.newIntent(this, mUser!!)
                startActivity(intent)
                return true
            }
            R.id.menu_search -> {
                val intent = SearchActivity.newIntent(this, mUser!!)
                startActivity(intent)
                return true
            }
            R.id.menu_logout -> {
                val intent = LoginActivity.newIntent(this, logout = true)
                startActivity(intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    companion object {

        private val INTENT_USER = "user"

        fun newIntent(context: Context, userId: String): Intent {
            val intent = Intent(context, ViewConcertsActivity::class.java)
            intent.putExtra(INTENT_USER, userId)
            return intent
        }
    }
}
