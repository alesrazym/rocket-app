package cz.quanti.razym.rocketapp.util

import android.icu.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

val firstFlightParser =
    SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

fun String.toDate(): Date? =
    runCatching {
        firstFlightParser.parse(this)
    }.getOrNull()

fun localeDateFormat(): DateFormat =
    DateFormat.getDateInstance(
        DateFormat.MEDIUM,
        Locale.getDefault(),
    )

fun Date.toLocalString(): String = localeDateFormat().format(this)
