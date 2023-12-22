package cz.quanti.razym.rocketapp.util

// TODO: Is there any advantage using android instead of java DateFormat and SimpleDateFormat?
//  Using Android requires additional test setup (mock), see `RocketListViewModelTest` test.
import android.icu.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

val dateFormatParserIso8601 =
    SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

fun String.toDate(): Date? =
    runCatching {
        dateFormatParserIso8601.parse(this)
    }.getOrNull()

fun localeDateFormat(): DateFormat =
    DateFormat.getDateInstance(
        DateFormat.MEDIUM,
        Locale.getDefault(),
    )

fun Date.toLocalString(): String = localeDateFormat().format(this)
