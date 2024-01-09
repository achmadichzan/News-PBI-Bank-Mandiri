package com.achmadichzan.newspbibankmandiri.util

import android.text.Spannable
import android.text.SpannableString
import android.text.style.LeadingMarginSpan

fun addIndentation(text: String?): Spannable {
    val indentedText = "\t \t${text}"
    val spannableString = SpannableString(indentedText)
    spannableString.setSpan(
        LeadingMarginSpan.Standard(0), 0, spannableString.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableString
}