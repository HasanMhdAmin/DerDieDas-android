package de.itshasan.derdiedas.component.popupActivity

import de.itshasan.derdiedas.callback.WordCallback
import de.itshasan.derdiedas.model.Word
import de.itshasan.derdiedas.parser.WordParser
import java.util.*

class PopUpPresenter(
    private val view: PopUpView,
    query: String,
) {

    init {
        getResult(query.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
    }

    private fun getResult(query: CharSequence) {

        WordParser.fetchWordData(query, object : WordCallback {

            override fun wordParsedSuccess(word: Word) {
                view.populateData(word)
            }

            override fun wordParsedFailed(error: String) {
                view.showError(error)
            }

        })
    }

}

interface PopUpView {
    fun populateData(word: Word)
    fun showError(error: String)
    fun showSoundIcon()
    fun hideSoundIcon()
}