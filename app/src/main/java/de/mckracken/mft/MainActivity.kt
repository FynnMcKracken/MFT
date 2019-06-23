package de.mckracken.mft

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import de.mckracken.mft.activities.SettingsActivity
import de.mckracken.mft.fragments.DevicesFragment
import de.mckracken.mft.fragments.ExpertFragment
import de.mckracken.mft.fragments.HomeFragment
import de.mckracken.mft.fragments.NewBluetoothFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_dashboard -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
            R.id.nav_devices -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, DevicesFragment()).commit()

            R.id.nav_diagnostics -> {

            }
            R.id.nav_expert -> {
                supportFragmentManager.beginTransaction().setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN ).replace(R.id.fragment_container, ExpertFragment()).commit()
            }
            R.id.nav_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))

            }
            R.id.nav_bluetooth -> {
                //TODO: This should be an activity
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, NewBluetoothFragment(this)).commit()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
