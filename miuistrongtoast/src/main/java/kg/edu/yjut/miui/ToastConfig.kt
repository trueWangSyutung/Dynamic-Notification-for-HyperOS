package kg.edu.yjut.miui

import android.app.PendingIntent
import android.widget.Toast

data class ToastConfig(
    var text: String,
    var textColor: String,
    var image : String ,
    var duration : Long = 3000L,
    var intent :  PendingIntent? = null
)