package kg.edu.yjut.litenote.miui.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.KeyEvent

class HomeKeyObserverReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            val action = intent!!.action
            val reason = intent.getStringExtra("reason")
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS == action && "homekey" == reason) {
                val keyCode = intent.getIntExtra("keycode", KeyEvent.KEYCODE_UNKNOWN)
                context?.stopService(Intent(context, DynamicIslandService::class.java))
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    companion object {
        private val TAG = HomeKeyObserverReceiver::class.java.simpleName
    }

}