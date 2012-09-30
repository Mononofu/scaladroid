package org.furidamu.scaladroid

import android.widget.SeekBar
import android.view.View
import android.widget.TextView
import android.widget.ListView
import android.widget.AdapterView
import android.app.ProgressDialog
import android.content.DialogInterface

import net.minidev.json.JSONValue
import net.minidev.json.JSONArray
import net.minidev.json.JSONObject

import _root_.android.util.Log

import scala.collection.JavaConversions._

object Helpers {
  // configure for your app
  val APP_NAME = "my_scala_app"
  val USE_GOOGLE_ANALYTICS = false
  val ANALYTICS_ID = "UA-xxxxxxxx-x"

  implicit def ViewToRichView(v: View) = new RichView(v)
  implicit def TextViewToRichTextView(v: TextView) = new RichTextview(v)
  implicit def ListViewToRichListView(v: ListView) = new RichListView(v)

  def log(str: String) {
    str match {
      case null => Log.d(APP_NAME, "null")
      case s => Log.d(APP_NAME, s)
    }
  }

  def runInBackground(f: => Unit) {
    new java.lang.Thread {
      override def run() {
        try { f }
        catch {
          case e =>
            log(Log.getStackTraceString(e))
        }
      }
    }.start()
  }

  def showProgressDialog(title: String, text: String, f: () => Unit = () => {})(implicit ctx: android.content.Context) = {
    ProgressDialog.show(ctx, title, text, true, true, new DialogInterface.OnCancelListener() {
      def onCancel(dialog: DialogInterface) {
        f
      }
    } )
  }

}

import Helpers._

class RichTextview(tv: TextView) {
  def text = tv.getText()
  def text_= (t: String) = tv.setText(t)
}

class RichView(view: View) {
  def onClick = throw new Exception
  def onClick_= (f: () => Unit) {
     view.setOnClickListener(
     new View.OnClickListener() {
      def onClick(v: View) {
        f()
      }
     } )
   }
  def findView[T](tr: TypedResource[T]) = view.findViewById(tr.id).asInstanceOf[T]
}


class RichListView(view: ListView) {
  def onItemClick = throw new Exception
   def onItemClick_= (f: (Int, Long) => Unit) {
     view.setOnItemClickListener(
     new AdapterView.OnItemClickListener() {
      def onItemClick(parent: AdapterView[_], v: View, position: Int, id: Long) {
        f(position, id)
      }
     } )
   }
}
