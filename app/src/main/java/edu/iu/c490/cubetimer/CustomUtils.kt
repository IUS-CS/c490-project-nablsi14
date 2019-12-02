package edu.iu.c490.cubetimer

import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

enum class TimeFormat{
    SHORT,
    LONG
}
class CustomUtils {

    companion object {

        fun formatTime(millis: Long, format: TimeFormat = TimeFormat.LONG): String {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - (minutes * 60)
            val mil = millis % 100

            if (format == TimeFormat.SHORT)
                return String.format("%02d.%02d", seconds, mil)

            return String.format("%02d:%02d.%02d", minutes, seconds, mil)

        }
        val dateFormatter = SimpleDateFormat("mm/dd/yy")

    }

}