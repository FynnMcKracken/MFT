package de.mckracken.mft

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import de.mckracken.mft.activities.SettingsActivity
import de.mckracken.mft.fragments.*
import de.mckracken.mft.manager.DMXManager
import de.mckracken.mft.manager.NewBluetoothManager
import de.mckracken.mft.viewmodel.ChannelsViewModel
import de.mckracken.mft.viewmodel.DiagnosticsViewModel
import de.mckracken.mft.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, LifecycleOwner {

    lateinit var viewModel: MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val dmxManager = DMXManager(this)
        (application as MultinoxApplication).dmxManager = dmxManager
        (application as MultinoxApplication).bluetoothManager = NewBluetoothManager(this, dmxManager)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        viewModel.bluetoothDevice.observe(this, Observer {
            if(it != null) {
                navView.getHeaderView(0).bluetooth_no_connection.visibility = View.GONE
                navView.getHeaderView(0).bluetooth_connected.visibility = View.VISIBLE
                navView.getHeaderView(0).connected_device_text_view.text = it.name
            }
            else {
                navView.getHeaderView(0).bluetooth_no_connection.visibility = View.VISIBLE
                navView.getHeaderView(0).bluetooth_connected.visibility = View.GONE
            }
        })

        navView.setNavigationItemSelectedListener(this)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment( channelsViewModel = ViewModelProviders.of(this).get(ChannelsViewModel::class.java))).commit()
        navView.menu.getItem(0).isChecked = true
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
            R.id.nav_dashboard -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment( channelsViewModel = ViewModelProviders.of(this).get(ChannelsViewModel::class.java))).commit()
            R.id.nav_devices -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, DevicesFragment()).commit()

            R.id.nav_diagnostics -> {
                val diagnosticsFragmentViewModel = ViewModelProviders.of(this).get(DiagnosticsViewModel::class.java)
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, DiagnosticsFragment(diagnosticsFragmentViewModel)).commit()
            }
            R.id.nav_expert -> {
                val expertFragmentViewModel = ViewModelProviders.of(this).get(ChannelsViewModel::class.java)
                supportFragmentManager.beginTransaction().setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN ).replace(R.id.fragment_container, ExpertFragment(expertFragmentViewModel)).commit()
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

    override fun onResume() {
        super.onResume()
    }
}
