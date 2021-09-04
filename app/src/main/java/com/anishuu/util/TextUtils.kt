package com.anishuu.util

import android.text.Html
import android.text.Spanned

/**
 * Converts the HTML text to a Spanned object.
 *
 * @param text The text to convert.
 * @return The HTML text as a Spanned object
 */
fun convertHtmlTextToSpanned(text: String?): Spanned {
    return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
}