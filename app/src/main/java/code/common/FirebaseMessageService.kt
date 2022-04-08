package code.common
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import code.basic.LoginActivity
import code.utils.AppSettings
import com.amtz.v2home.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONException
import org.json.JSONObject

//Firebase Push notification
class FirebaseMessageService : FirebaseMessagingService() {
    var CHANNEL_ID = "com.amtz.v2home"
    private var mManager: NotificationManager? = null
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle data payload of FCM messages.
        Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId())
        Log.d(
            TAG,
            "FCM Notification Message: " + remoteMessage.getData()
                .toString() + "...." + remoteMessage.getFrom()
        )
        remoteMessage.getData()
        val params: Map<String?, String?> = remoteMessage.getData()
        val json = JSONObject(params)

        //Log.v("qljhslhqjs", String.valueOf(json));
        var message: String? = ""
        val intent = Intent(this.getPackageName())
        try {
            if (json.has("message")) message = json.getString("message")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        createNotification(message, this.getString(R.string.app_name))
    }

    fun createNotification(msg: String?, title: String?) {
        var intent: Intent? = null
        intent = Intent(this, LoginActivity::class.java)
        val contentIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val androidChannel = NotificationChannel(
                CHANNEL_ID,
                title, NotificationManager.IMPORTANCE_HIGH
            )
            // Sets whether notifications posted to this channel should display notification lights
            androidChannel.enableLights(true)
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true)
            // Sets the notification light color for notifications posted to this channel
            androidChannel.lightColor = Color.GREEN
            //androidChannel.setSound(null, null);

            // Sets whether notifications posted to this channel appear on the lockScreen or not
            androidChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            manager!!.createNotificationChannel(androidChannel)
            val notification = NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(title)
                .setContentText(msg)
                .setTicker(title)
                .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
                .setSound(defaultUri)
                .setContentIntent(contentIntent)
            val timestamp = 1000
            manager!!.notify(timestamp, notification.build())
        } else {
            try {
                playNotificationSound()
                @SuppressLint("NewApi", "LocalSuppress") val notificationBuilder =
                    NotificationCompat.Builder(this)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle(title)
                        .setTicker(title)
                        .setContentText(msg)
                        .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentIntent(contentIntent)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setLights(-0x89fe6d, 300, 1000)
                        .setAutoCancel(true).setVibrate(longArrayOf(200, 400))
                val timestamp = 1000
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(
                    timestamp /* ID of notification */,
                    notificationBuilder.build()
                )
            } catch (se: SecurityException) {
                se.printStackTrace()
            }
        }
    }

    private val manager: NotificationManager?
        private get() {
            if (mManager == null) {
                mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            }
            return mManager
        }

    // Playing notification sound
    fun playNotificationSound() {
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(this, notification)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
    }

    companion object {
        private const val TAG = "MyFMService"
    }
}