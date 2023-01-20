package gr.divinelink.core.util.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

object DateUtil {
    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): String {
        val date = Date()
        val formatter = SimpleDateFormat("dd MMMM yyyy")
        return formatter.format(date.time)
    }
}
