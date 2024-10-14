package com.example.stressmeter

import android.os.Bundle
import android.view.Menu
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.stressmeter.databinding.ActivityMainBinding
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var mediaPlayer: MediaPlayer
    private var isSoundPlaying = false
    private lateinit var vibrator: Vibrator


    //TODO: NEED TO CREATE NOTIFICATION ON OPEN
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        mediaPlayer = MediaPlayer.create(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                playNotification()
            } else {
                // Permission denied; handle it accordingly.
            }
        }
        requestNotificationPermission()
        playNotification()


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
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


//    private fun playNotification() {
//        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val ringtone = RingtoneManager.getRingtone(applicationContext, notification)
//        ringtone.play()
//    }

    private fun playNotification() {
        if (!isSoundPlaying) {
            isSoundPlaying = true
            Thread {
                //sound
                val mediaPlayer = MediaPlayer.create(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                mediaPlayer.isLooping = true
                mediaPlayer.start()

                vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
                    Log.d("Vibrate", "VIBRATING RIGHT NOW")
                } else {
                    val pattern = longArrayOf(0,1000,100)
                    vibrator.vibrate(pattern, 0)
                }

                // play the notification
                while (isSoundPlaying) {

                }
                mediaPlayer.stop()
                mediaPlayer.release()
            }.start()
        }
    }

    fun stopNotification() {
        if (isSoundPlaying) {
            mediaPlayer.stop()
            mediaPlayer.reset()
            isSoundPlaying = false
            vibrator.cancel()
            Log.d("CANCEL NOTIF", "NOTIFICATION CANCELLED")
        }
    }

    private fun requestNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted yet, request it.
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // Permission is already granted, dont do anything
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopNotification()
    }

    override fun onPause() {
        super.onPause()
        stopNotification()
        isSoundPlaying = false
    }
}