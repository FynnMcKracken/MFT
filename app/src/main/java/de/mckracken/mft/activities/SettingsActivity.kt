package de.mckracken.mft.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import de.mckracken.mft.MultinoxApplication
import de.mckracken.mft.R
import de.mckracken.mft.manager.DMXManager
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(settings_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Settings"
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            when(key) {
                "device_display" -> (application as MultinoxApplication).bluetoothManager.write(DMXManager.getDisplayPacket(prefs.getBoolean(key, true)))
                "ip_adress" -> (application as MultinoxApplication).bluetoothManager.write(DMXManager.getIPAdressPacket(prefs.getString(key, "1.1.1.1") ?: "1.1.1.1"))
                "art_net" -> (application as MultinoxApplication).bluetoothManager.write(DMXManager.getModePacket(if(prefs.getBoolean(key, false)) 2 else 1))
            }
        }
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}