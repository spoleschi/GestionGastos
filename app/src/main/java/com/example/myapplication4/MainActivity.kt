package com.example.myapplication4

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication4.clases.Usuario
import com.example.myapplication4.databinding.ActivityMainBinding
import com.example.myapplication4.notifications.NotificationWorker

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

//        binding.appBarMain.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .setAnchorView(R.id.fab).show()
//        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                //R.id.nav_home, R.id.nav_transaction, R.id.nav_transactions,R.id.nav_category,R.id.nav_cotizacion_dolar
                R.id.nav_home, R.id.nav_transactions,R.id.nav_category,R.id.nav_cotizacion_dolar
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Request notification permission for Android 13 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        } else {
            scheduleNotifications()
        }

        val nuevoUser = Usuario(
            1,
            "a",
            "b",
            "c",
            10f
        );

        val totalAmount: TextView = findViewById(R.id.totalAmount);

        totalAmount.text = "$" + nuevoUser.saldo.toString();

        nuevoUser.actualizarSaldo(50f);

        //Se tiene que volver a definir el valor del textView
        totalAmount.text = "$" + nuevoUser.saldo.toString();
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                scheduleNotifications()
            }
        }.launch(android.Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun scheduleNotifications() {
        NotificationWorker.schedule(this)
    }

}