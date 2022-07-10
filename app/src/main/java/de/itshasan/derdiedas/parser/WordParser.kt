package de.itshasan.derdiedas.parser

import android.util.Log
import de.itshasan.derdiedas.callback.WordCallback
import de.itshasan.derdiedas.model.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

private val TAG = WordParser::class.java.simpleName

object WordParser {

    private val derDieDas = arrayOf("der", "die", "das")

    fun fetchWordData(keyword: CharSequence, callback: WordCallback) {

        GlobalScope.launch(Dispatchers.IO) {
            derDieDas.forEachIndexed { index, item ->


                val url = "https://der-artikel.de/$item/$keyword.html"
                Log.d(TAG, "fetchWordData: url: $url")
                try {
                    val document = Jsoup.connect(url).get()
                    val body = document.body()
                    val headerElement = body.select("header.masthead")
                        .select("div.container")

                    val tableElement = body.select("table.table").toString()


                    val art = headerElement.select("h1.mb-1").select("span").first().text()
                    val word = headerElement.select("h1.mb-1").text().replace(art, "").trim()

                    val pos = headerElement.select("h3").select("em").text()
                    val toRemove = headerElement.select("h3").select("span").select("i").text()
                    val translation =
                        headerElement.select("h3").select("span").text().replace(toRemove, "")

                    val wordResult = Word(article = art,
                        word = word,
                        pos = pos,
                        translation = translation,
                        declination = tableElement)
                    GlobalScope.launch(Dispatchers.Main) {
                        callback.wordParsedSuccess(wordResult)
                    }
                    return@launch
                } catch (e: Exception) {

                    if (index == 2) {
                        GlobalScope.launch(Dispatchers.Main) {
                            callback.wordParsedFailed(e.message ?: "")
                        }

                        Log.e(TAG, "fetchWordData: exc: $e")
                    }
                }

            }

        }

    }

}