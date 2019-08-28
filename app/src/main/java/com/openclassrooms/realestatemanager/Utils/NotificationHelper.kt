package com.openclassrooms.realestatemanager.Utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.openclassrooms.realestatemanager.R

class NotificationHelper {

    companion object {
        private const val APP_CHANNEL_ID = "RealEstateManager"

        fun sendNotification(context: Context){
            this.createNotificationChannel(context)
            this.createNotification(context)
        }

        private fun createNotificationChannel(context: Context) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context.getString(R.string.notification_channel_name)
                val descriptionText = context.getString(R.string.notification_channel_description)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(APP_CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager = getSystemService(context, NotificationManager::class.java) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }


        private fun createNotification(context: Context){
            val builder = NotificationCompat.Builder(context, APP_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_twotone_home_black_24px)
                    .setContentTitle(context.getString(R.string.notification_adding_success_title))
                    .setContentText(context.getString(R.string.notification_adding_success_message))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)

            // Show Notification
            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(1, builder.build())
            }
        }
    }
}