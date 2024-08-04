package com.innovatech.netbeltktz

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class MyApp : Application() {
    companion object {
        const val NOTIFICATION_CHANNEL_ID = "notification_fcm"
    }

    override fun onCreate() {
        super.onCreate()

        Firebase.messaging.token.addOnCompleteListener {
            if (!it.isSuccessful) {
                println("El token no fue generado")
                return@addOnCompleteListener
            }
            val token = it.result
            println("El token es> $token")
        }
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ALERTA!"
            val descriptionText = "El dispositivo ha detecado una caída"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                setShowBadge(true)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}