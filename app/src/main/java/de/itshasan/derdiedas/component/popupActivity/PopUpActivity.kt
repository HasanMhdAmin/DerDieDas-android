package de.itshasan.derdiedas.component.popupActivity

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Base64
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import de.itshasan.derdiedas.R
import de.itshasan.derdiedas.model.Word

class PopUpActivity : AppCompatActivity(), PopUpView {

    private val wordTextView by lazy { findViewById<TextView>(R.id.wordTextView) }
    private val posTextView by lazy { findViewById<TextView>(R.id.posTextView) }
    private val translationTextView by lazy { findViewById<TextView>(R.id.translationTextView) }

    private val webView by lazy { findViewById<WebView>(R.id.webView) }
    private val parentView by lazy { findViewById<ConstraintLayout>(R.id.parentView) }

    private lateinit var presenter: PopUpPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val query = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        val queryString = query.toString()
        presenter = PopUpPresenter(this, queryString)

        parentView.setOnClickListener {
            finish()
        }

    }

    override fun populateData(word: Word) {
        val result = "<span style=\"color:red;\">${word.article}</span> ${word.word}"
        wordTextView.text = Html.fromHtml(result, Html.FROM_HTML_MODE_COMPACT)
        posTextView.text = word.pos
        translationTextView.text = word.translation
        val webViewData =
            "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">" +
                word.declination
                    .replace("style=\"text-align: center;\"", "")
                    .replace("onclick=\"showModal('nomdas')\"", "")
                    .replace("onclick=\"showModal('gendas')\"", "")
                    .replace("onclick=\"showModal('datdas')\"", "")
                    .replace("onclick=\"showModal('akkdas')\"", "")
        val base64 = Base64.encodeToString(webViewData.encodeToByteArray(), Base64.DEFAULT)
        webView.loadData(base64, "text/html; charset=utf-8", "base64")

    }

    override fun showError(error: String) {
    }

    override fun showSoundIcon() {}

    override fun hideSoundIcon() {}


}