package org.furidamu.scaladroid

import _root_.android.widget.Toast
import _root_.android.app.Activity
import _root_.android.os.Bundle
import _root_.android.util.Log
import android.app.NotificationManager
import android.content.Context
import android.app.Notification
import android.content.Intent
import android.app.PendingIntent

import com.google.android.apps.analytics.GoogleAnalyticsTracker

import Helpers._

class ScalaActivity extends Activity with TypedActivity {
  var settings: android.content.SharedPreferences = _
  var tracker: GoogleAnalyticsTracker = _

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)
    settings = getSharedPreferences(APP_NAME, 0)

    if(USE_GOOGLE_ANALYTICS) {
      tracker = GoogleAnalyticsTracker.getInstance()
      tracker.setDebug(true)
      tracker.startNewSession(ANALYTICS_ID, 20, this)

      tracker.trackPageView("/android/" + this.getClass.getName)
    }
  }

  def trackPage(url: String, device: String = "", domain: String = "") {
    if(device != "")
      tracker.setCustomVar(1, "device", device)
    if(domain != "")
      tracker.setCustomVar(2, "domain", domain)

    tracker.trackPageView(url)
  }

  def runOnUiThread(f: => Unit) {
    super.runOnUiThread(new Runnable() {
        def run() {
          try { f }
          catch {
            case e =>
              log(Log.getStackTraceString(e))
          }
        }
      })
  }

  def showPopup(s: String) {
    runOnUiThread {
      Toast.makeText(
        getApplicationContext(),
        s,
        Toast.LENGTH_SHORT).show()
    }
  }

  override def onDestroy() {
    super.onDestroy()
    if(USE_GOOGLE_ANALYTICS) {
      tracker.dispatch()
      tracker.stopSession()
    }
  }

  def showNotification[T](title: String, content: String, onClickActivity: Class[T]) {
    val tickerText = title + ": " + content
    val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) match {
      case s: NotificationManager => s
    }

    // open MainActivity if notification is tapped
    val notificationIntent = new Intent(this, onClickActivity);
    val contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

    val notification = new Notification.Builder(getApplicationContext())
         .setContentTitle(title)
         .setContentText(content)
         .setTicker(title + ": " + content)
         .setSmallIcon(R.drawable.icon)       // change this to whatever icon you want
         .setAutoCancel(true)
         .setContentIntent(contentIntent)
         .getNotification()

    val HELLO_ID = 1;
    mNotificationManager.notify(HELLO_ID, notification);
  }
}

