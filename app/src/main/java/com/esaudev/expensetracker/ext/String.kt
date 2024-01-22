package com.esaudev.expensetracker.ext

val specialCharactersRegex = Regex("[^A-Za-z0-9 ]")

fun String.hasSpecialCharacters(): Boolean {
    return specialCharactersRegex.containsMatchIn(this)
}
