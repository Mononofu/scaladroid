package org.furidamu.scaladroid

import _root_.android.os.Bundle
import Helpers._

class MainActivity extends ScalaActivity {
  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)
    setContentView(R.layout.main)

    showNotification("I'm running", "really, believe me", classOf[MainActivity])

    runInBackground {
      // won't block foreground
      Thread.sleep(3000)

      runOnUiThread {
        // only UI Thread can display stuff
        showPopup("done sleeping!")
        findView(TR.hellotext).text = "well slept"
      }
    }

    showPopup("see, I'm not blocked by the sleep")
  }
}
