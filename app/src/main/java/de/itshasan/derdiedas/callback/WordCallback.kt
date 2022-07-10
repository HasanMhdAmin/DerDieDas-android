package de.itshasan.derdiedas.callback

import de.itshasan.derdiedas.model.Word

interface WordCallback {
    fun wordParsedSuccess(word: Word)
    fun wordParsedFailed(error: String)
}