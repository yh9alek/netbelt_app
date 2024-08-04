package com.innovatech.netbeltktz

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.data.isNotEmpty()) {
            // Handle data payload
            showNotification(message.data["title"], message.data["body"])
        } else if (message.notification != null) {
            // Handle notification payload if no data payload is provided
            showNotification(message.notification?.title, message.notification?.body)
        }
        val intent = Intent(this, DropActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    private fun showNotification(title: String?, body: String?) {
        val notificationManager = getSystemService(NotificationManager::class.java)

        // Crear un intent para abrir la actividad deseada al hacer clic en la notificación
        val intent = Intent(this, DropActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        // Construir la notificación
        val notification = NotificationCompat.Builder(this, MyApp.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.alert)
            .setPriority(NotificationCompat.PRIORITY_MAX) // Máxima prioridad
            .setDefaults(Notification.DEFAULT_ALL) // Activar sonido, luz y vibración
            .setContentIntent(pendingIntent)
            .setAutoCancel(false) // No permitir que se cancele automáticamente
            .setStyle(NotificationCompat.BigTextStyle().bigText(body)) // Estilo personalizado
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Hacer visible en la pantalla de bloqueo
            .setFullScreenIntent(pendingIntent, true) // Heads-up notification
            .build()

        notificationManager.notify(1, notification)
    }
}