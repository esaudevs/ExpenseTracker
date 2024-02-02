package com.esaudev.expensetracker.ext

import java.util.Locale

val specialCharactersRegex = Regex("[^A-Za-z0-9 ]")

fun String.hasSpecialCharacters(): Boolean {
    return specialCharactersRegex.containsMatchIn(this)
}

fun String.toSentence(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
}
