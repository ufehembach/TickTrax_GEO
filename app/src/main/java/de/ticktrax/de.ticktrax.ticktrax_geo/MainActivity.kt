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
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import de.ticktrax.de.ticktrax.ticktrax_geo.data.datamodels.ALogType
import de.ticktrax.de.ticktrax.ticktrax_geo.hasLocationPermission
import de.ticktrax.de.ticktrax.ticktrax_geo.location.LocationService
import kotlinx.coroutines.selects.select
import de.ticktrax.ticktrax_geo.databinding.ActivityMainBinding
import de.ticktrax.ticktrax_geo.ui.Home_Fragment
import de.ticktrax.ticktrax_geo.ui.Home_FragmentDirections
import de.ticktrax.ticktrax_geo.ui.TickTraxViewModel
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private val viewModel: TickTraxViewModel by viewModels()

    // ufe stuff
    init {
        Log.d("ufe-geo", "mainactivity init")
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

        viewModel.aLog(ALogType.INFO,"################# TZ:"+ Locale.getDefault())
        viewModel.aLog(ALogType.INFO,"App Started")

        //NavController durch NavHostFragment laden
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostHomeFrag) as NavHostFragment

        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        //Wird aufgerufen wenn Item in der NavBar ausgewählt wird
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            Log.d("ufe", "BottomNavSelectList " + item.itemId)

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
            Log.d("ufe-geo", "HamburgerSelectList " + it.toString())
            //("ufe-geo", "HamburgerSelectList " + it.toString())
            when (it.itemId) {
                R.id.HMenuHome ->
                    Toast.makeText(this, R.string.MenuHome, Toast.LENGTH_SHORT).show()

                R.id.HMenuMe ->
                    Toast.makeText(this, R.string.MenuMe, Toast.LENGTH_SHORT).show()

                R.id.HMenuExport ->
                    Toast.makeText(this, R.string.MenuExport, Toast.LENGTH_SHORT).show()

                R.id.HMenuSettings ->
                    Toast.makeText(this, R.string.MenuSettings, Toast.LENGTH_SHORT).show()

                R.id.HMenuGeo ->
                    Toast.makeText(this, R.string.MenuGEO, Toast.LENGTH_SHORT).show()

                R.id.HMenuLog -> {
                    Log.d("ufe", "Hamburger nav to log" + it.toString())
                    // ("ufe", "Hamburger nav to log"+it.toString())
                    Toast.makeText(this, "pre- " + R.string.HMenuLog, Toast.LENGTH_SHORT).show()
                    // val navController = findNavController(R.id.navHostHomeFrag)
                    // navController.navigate(R.id.ALogFragment)
                    navHostFragment.navController.navigate(R.id.ALogFragment)
                    Log.d("ufe", "Hamburger after nav to log" + it.toString())
                    Toast.makeText(this, R.string.HMenuLog, Toast.LENGTH_SHORT).show()
                    hamDrawerLayout.closeDrawer(GravityCompat.START)
                }

                R.id.HMenuLicense -> {
                    Toast.makeText(this, R.string.HMenuLicense, Toast.LENGTH_SHORT).show()
                    // When the user selects an option to see the licenses:
                    startActivity(Intent(this, OssLicensesMenuActivity::class.java))
                }

                R.id.HMenuCopy ->
                    Toast.makeText(this, R.string.HMenuCopy, Toast.LENGTH_SHORT).show()

                R.id.HMenuImpressum ->
                    Toast.makeText(this, R.string.HMenuImpressum, Toast.LENGTH_SHORT).show()

            }

            return@setNavigationItemSelectedListener false
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
        viewModel.aLog(ALogType.GEO,"main on start, sent start intend")
        Log.d("ufe-geo", "main on start, sent start intend")
        if (!this.hasLocationPermission()) {
            viewModel.aLog(ALogType.GEO,"No Location Permissions")
            Log.d("ufe-geo", "No Location Permissions")
        } else
            viewModel.aLog(ALogType.GEO,"LocationService Intent - START")
            Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                startService(this)
            }
    }

    override fun onStop() {
        Log.d("ufe-geo", "main on stop")
        viewModel.aLog(ALogType.GEO,"main-onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("ufe-geo", "main on stop, sent stop intend")
        viewModel.aLog(ALogType.GEO,"LocationService Intent - STOP")
        Intent(applicationContext, LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
            startService(this)
        }
        super.onDestroy()
    }


}
