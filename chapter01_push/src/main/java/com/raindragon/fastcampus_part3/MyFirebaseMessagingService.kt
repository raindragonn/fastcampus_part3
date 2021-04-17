package com.raindragon.fastcampus_part3

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

// Created by raindragonn on 2021/04/16.

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널"
        private const val CHANNEL_ID = "Channel Id"
    }

    // token 은 변경 될 가능성이 높다.
    // 일반적은 서비스 앱은 토큰이 변경시 서버로 토큰을 알려준다.

    override fun onNewToken(nToken: String) {
        super.onNewToken(nToken)
    }

    // 메시지가 올때 콜백
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        createNotificationChannel()

        val type = remoteMessage.data["type"]?.let {
            NotificationType.valueOf(it)
        }
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]

        type ?: return

        NotificationManagerCompat.from(this)
            .notify(type.id, createNotification(type, title, message))
    }

    private fun createNotification(
        type: NotificationType,
        title: String?,
        message: String?
    ): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("notificationType", "${type.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        // 동일한 requestCode 를 가진 pendingIntent 라면 flag 에 따라 동작을 달리 할 수 있다.
        val pendingIntent = PendingIntent.getActivity(this, type.id, intent, FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        when (type) {
            NotificationType.NORMAL -> Unit
            NotificationType.EXPANDABLE -> {
                notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(
                            "😀 😃 😄 😁 😆 😅 😂 🤣 🥲" +
                                    " ☺️ 😊 😇 🙂 🙃 😉 😌 😍 🥰 😘 😗" +
                                    " 😙 😚 😋 😛 😝 😜 🤪 🤨 🧐 🤓 😎 🥸 " +
                                    "🤩 🥳 😏 😒 😞 😔 😟 😕 🙁 ☹️ 😣 " +
                                    "😖 😫 😩 🥺 😢 😭 😤 😠 😡 🤬 🤯 " +
                                    "😳 🥵 🥶 😱 😨 😰 😥 😓 🤗 🤔 🤭 " +
                                    "🤫 🤥 😶 😐 😑 😬 🙄 😯 😦 😧 😮 " +
                                    "😲 🥱 😴 🤤 😪 😵 🤐 🥴 🤢 🤮 🤧 " +
                                    "😷 🤒 🤕 🤑 🤠 😈 👿 👹 👺 🤡 💩 " +
                                    "👻 💀 ☠️ 👽 👾 🤖 🎃 😺 😸 😹 😻 " +
                                    "😼 😽 🙀 😿 😾"
                        )
                )
            }
            NotificationType.CUSTOM -> {
                notificationBuilder
                    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(
                        RemoteViews(
                            packageName,
                            R.layout.view_notification
                        ).apply {
                            setTextViewText(R.id.tv_title, title)
                            setTextViewText(R.id.tv_message, message)
                        }
                    )
            }
        }
        return notificationBuilder.build()
    }

    private fun createNotificationChannel() {
        // 8.0 이후에는 채널을 만들어야 한다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)


        }
    }
}