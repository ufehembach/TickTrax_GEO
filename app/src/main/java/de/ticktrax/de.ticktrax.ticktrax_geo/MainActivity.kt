package de.ticktrax.ticktrax_geo

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import de.ticktrax.de.ticktrax.ticktrax_geo.location.LocationService
import kotlinx.coroutines.selects.select


import de.ticktrax.ticktrax_geo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    // ufe stuff
    init {
        Log.d("ufe-geo", "bla")
    }

    private lateinit var hamDrawerLayout: DrawerLayout
    private lateinit var hamNavigationView: NavigationView
    private lateinit var hamDrawerToggle: ActionBarDrawerToggle

    private lateinit var binding: ActivityMainBinding
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (hamDrawerToggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ufe-geo", "onCreate")

        // ufe stuff
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//hamburger
        Log.d("ufe-geo", "Hamburger")
        hamDrawerLayout = binding.hamDrawerLayout
        hamNavigationView = binding.hamburgerNav
        hamDrawerToggle =
            ActionBarDrawerToggle(this, hamDrawerLayout, R.string.openHam, R.string.closeHam)
        hamDrawerLayout.addDrawerListener(hamDrawerToggle)
        hamDrawerToggle.syncState()
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        hamNavigationView.setNavigationItemSelectedListener() {
            Log.d("ufe-geo", "HamburgerSelectList")
            when (it.itemId) {
                R.id.home_Fragment2 ->
                    Toast.makeText(this, "hamburger home", Toast.LENGTH_SHORT).show()

                R.id.me_Fragment2 ->
                    Toast.makeText(this, "hamburger me", Toast.LENGTH_SHORT).show()

                R.id.settings_Fragment2 ->
                    Toast.makeText(this, "hamburger settings", Toast.LENGTH_SHORT).show()

                R.id.export_Fragment2 ->
                    Toast.makeText(this, "hamburger export", Toast.LENGTH_SHORT).show()
            }
            return@setNavigationItemSelectedListener false
        }
        //NavController durch NavHostFragment laden
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostHomeFrag) as NavHostFragment

        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        //Wird aufgerufen wenn Item in der NavBar ausgewählt wird
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            Log.d("ufe-geo", "BottomNavSelectList")

            //Standard NavBar Funktionalität: Navigiere zum ausgewählten Item
            //Hierbei wird auch im navController der entsprechende BackStack geladen
            //Das führt dazu dass vorherige Navigation noch "gespeichert" und z.B.
            //das zweite Fragment angezeigt wird obwohl das erste ausgewählt wurde
            NavigationUI.onNavDestinationSelected(item, navController)

            //Hier lösen wir das Problem indem wir den BackStack zurücksetzen auf das ausgewählte Item
            navController.popBackStack(item.itemId, false)
            Log.w("Navbar", item.toString())

            //Toast.makeText(this@MainActivity, "selected Item: " + item.toString(), Toast.LENGTH_LONG).show()

            //Item soll als ausgewählt angezeigt werden(farblich hinterlegt)
            return@setOnItemSelectedListener true
        }



        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigateUp()
                Toast.makeText(this@MainActivity, "Back Pressed", Toast.LENGTH_LONG).show()
            }

        })

        fun onBackPressed() {
            if (hamDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                hamDrawerLayout.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0
        )
    }

    override fun onStart() {
        super.onStart()
        Intent(applicationContext, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            startService(this)
        }
    }

    override fun onStop() {
        Intent(applicationContext, LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
            startService(this)
        }
        super.onStop()
    }
}
