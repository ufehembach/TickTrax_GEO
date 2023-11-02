package de.ticktrax.ticktrax_geo

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
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
import de.ticktrax.ticktrax_geo.data.datamodels.ALogType
import de.ticktrax.ticktrax_geo.hasLocationPermission
import de.ticktrax.ticktrax_geo.location.LocationService
import kotlinx.coroutines.selects.select
import de.ticktrax.ticktrax_geo.databinding.ActivityMainBinding
import de.ticktrax.ticktrax_geo.ui.Home_Fragment
import de.ticktrax.ticktrax_geo.ui.Home_FragmentDirections
import de.ticktrax.ticktrax_geo.ui.TickTraxViewModel
import java.util.Locale


import android.content.Context
import android.content.pm.PackageManager
import de.ticktrax.ticktrax_geo.myTools.logDebug
import org.osmdroid.config.Configuration
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

class MainActivity : AppCompatActivity() {
    private val viewModel: TickTraxViewModel by viewModels()

    // ufe stuff
    init {
        logDebug("ufe-geo", "mainactivity init")
    }

    private lateinit var hamDrawerLayout: DrawerLayout
    private lateinit var hamNavigationView: NavigationView
    private lateinit var hamDrawerToggle: ActionBarDrawerToggle

    public lateinit var map: MapView

    private lateinit var binding: ActivityMainBinding
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (hamDrawerToggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logDebug("ufe-geo", "onCreate")

        // This won't work unless you have imported this: org.osmdroid.config.Configuration.*
        //   getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        Configuration.getInstance().setUserAgentValue(this?.getPackageName() ?: "TickTrax.de");

        // ufe stuff
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.aLog(ALogType.INFO, "################# TZ:" + Locale.getDefault())
        viewModel.aLog(ALogType.INFO, "App Started")

        //NavController durch NavHostFragment laden
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostHomeFrag) as NavHostFragment

        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        //Wird aufgerufen wenn Item in der NavBar ausgewählt wird
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            //logDebug("ufe", "BottomNavSelectList " + item.itemId)
            //Standard NavBar Funktionalität: Navigiere zum ausgewählten Item
            //Hierbei wird auch im navController der entsprechende BackStack geladen
            //Das führt dazu dass vorherige Navigation noch "gespeichert" und z.B.
            //das zweite Fragment angezeigt wird obwohl das erste ausgewählt wurde
            NavigationUI.onNavDestinationSelected(item, navController)
            //Hier lösen wir das Problem indem wir den BackStack zurücksetzen auf das ausgewählte Item
            navController.popBackStack(item.itemId, false)
          //  logDebug("Navbar", item.toString())
            //Toast.makeText(this@MainActivity, "selected Item: " + item.toString(), Toast.LENGTH_LONG).show()
            //Item soll als ausgewählt angezeigt werden(farblich hinterlegt)
            return@setOnItemSelectedListener true
        }

        //hamburger
       // logDebug("ufe-geo", "Hamburger")
        hamDrawerLayout = binding.hamDrawerLayout
        hamNavigationView = binding.hamburgerNav
        hamDrawerToggle =
            ActionBarDrawerToggle(this, hamDrawerLayout, R.string.openHam, R.string.closeHam)
        hamDrawerLayout.addDrawerListener(hamDrawerToggle)
        hamDrawerToggle.syncState()
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        hamNavigationView.setNavigationItemSelectedListener() {
         //   logDebug("ufe-geo", "HamburgerSelectList " + it.toString())
            //("ufe-geo", "HamburgerSelectList " + it.toString())
            when (it.itemId) {
                R.id.HMenuHome -> {
                    navHostFragment.navController.navigate(R.id.home_Fragment)
                    Toast.makeText(this, R.string.MenuHome, Toast.LENGTH_SHORT).show()
                    hamDrawerLayout.closeDrawer(GravityCompat.START)
                }

                R.id.HMenuPlaces -> {
                    navHostFragment.navController.navigate(R.id.places_Fragment)
                    Toast.makeText(this, R.string.MenuPlaces, Toast.LENGTH_SHORT).show()
                    hamDrawerLayout.closeDrawer(GravityCompat.START)
                }

                R.id.HMenuLocations -> {
                    navHostFragment.navController.navigate(R.id.locations_Fragment)
                    Toast.makeText(this, R.string.MenuLocations, Toast.LENGTH_SHORT).show()
                    hamDrawerLayout.closeDrawer(GravityCompat.START)
                }

                R.id.HMenuGeo -> {
                    navHostFragment.navController.navigate(R.id.GEO_Fragment)
                    Toast.makeText(this, R.string.MenuGEO, Toast.LENGTH_SHORT).show()
                    hamDrawerLayout.closeDrawer(GravityCompat.START)
                }

                R.id.HMenuExport -> {
                    navHostFragment.navController.navigate(R.id.export2Mail_Fragment)
                    Toast.makeText(this, R.string.MenuExport, Toast.LENGTH_SHORT).show()
                    hamDrawerLayout.closeDrawer(GravityCompat.START)

                }

                R.id.HMenuLog -> {
                    // ("ufe", "Hamburger nav to log"+it.toString())
                    Toast.makeText(this, "pre- " + R.string.HMenuLog, Toast.LENGTH_SHORT)
                        .show()
                    // val navController = findNavController(R.id.navHostHomeFrag)
                    // navController.navigate(R.id.ALogFragment)
                    navHostFragment.navController.navigate(R.id.ALogFragment)
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
                Toast.makeText(this@MainActivity, "Back Pressed", Toast.LENGTH_LONG)
                    .show()
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
        viewModel.aLog(ALogType.GEO, "main on start, sent start intend")
        logDebug("ufe-geo", "main on start, sent start intend")
        if (!this.hasLocationPermission()) {
            viewModel.aLog(ALogType.GEO, "No Location Permissions")
            logDebug("ufe-geo", "No Location Permissions")
            Toast.makeText(this, R.string.PermissionToast, Toast.LENGTH_LONG).show()
            Toast.makeText(this, R.string.PermissionToast, Toast.LENGTH_LONG).show()
            Toast.makeText(this, R.string.PermissionToast, Toast.LENGTH_LONG).show()
        } else
        //    viewModel.aLog(ALogType.GEO, "LocationService Intent - START")
        Intent(applicationContext, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            startService(this)
        }
    }

    override fun onStop() {
        logDebug("ufe-geo", "main on stop")
        viewModel.aLog(ALogType.GEO, "main-onStop")
        super.onStop()
    }

    override fun onDestroy() {
        logDebug("ufe-geo", "main on stop, sent stop intend")
        viewModel.aLog(ALogType.GEO, "LocationService Intent - STOP")
        Intent(applicationContext, LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
            startService(this)
        }
        super.onDestroy()
    }
//    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        val permissionsToRequest = ArrayList<String>()
//        var i = 0
//        while (i < grantResults.size) {
//            permissionsToRequest.add(permissions[i])
//            i++
//        }
//        if (permissionsToRequest.size > 0) {
//            ActivityCompat.requestPermissions(
//                this,
//                permissionsToRequest.toTypedArray(),
//                REQUEST_PERMISSIONS_REQUEST_CODE)
//        }
//    }

}
