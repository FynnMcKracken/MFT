package de.mckracken.mft

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import de.mckracken.mft.fragments.BluetoothFragment
import de.mckracken.mft.fragments.DevicesFragment
import de.mckracken.mft.fragments.NewBluetoothFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_bluetooth -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, NewBluetoothFragment(this)).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_list -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, DevicesFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

}
