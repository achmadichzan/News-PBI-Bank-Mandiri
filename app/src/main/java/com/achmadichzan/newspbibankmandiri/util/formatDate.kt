package com.achmadichzan.newspbibankmandiri.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

fun formatDate(inputDate: String): String? {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())

    try {
        val date = inputFormat.parse(inputDate)
        return date?.let { outputFormat.format(it) }
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return inputDate
}