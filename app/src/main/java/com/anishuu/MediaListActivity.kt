package com.anishuu

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.rosati.anishuu.R

class MediaListActivity : AppCompatActivity() {
    private lateinit var titleEditView: EditText
    private lateinit var numVolumeEditView: EditText
    private lateinit var languageEditView: EditText
    private lateinit var authorEditView: EditText
    private lateinit var publisherEditView: EditText
    private lateinit var notesEditView: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_list)

        titleEditView = findViewById(R.id.title_entry)
        numVolumeEditView = findViewById(R.id.num_volumes_entry)
        languageEditView = findViewById(R.id.language_entry)
        authorEditView = findViewById(R.id.author_entry)
        publisherEditView = findViewById(R.id.publisher_entry)
        notesEditView = findViewById(R.id.notes_entry)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(titleEditView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val title = titleEditView.text.toString()
                val numVolumes = numVolumeEditView.text.toString().toInt()
                val language = languageEditView.text.toString()
                val author = authorEditView.text.toString()
                val publisher = publisherEditView.text.toString()
                val notes = notesEditView.text.toString()

                replyIntent.putExtra(EXTRA_TITLE, title)
                replyIntent.putExtra(EXTRA_NUM_VOLUMES, numVolumes)
                replyIntent.putExtra(EXTRA_LANGUAGE, language)
                replyIntent.putExtra(EXTRA_AUTHOR, author)
                replyIntent.putExtra(EXTRA_PUBLISHER, publisher)
                replyIntent.putExtra(EXTRA_NOTES, notes)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_TITLE = "com.anishuu.TITLE"
        const val EXTRA_NUM_VOLUMES = "com.anishuu.NUM_VOLUMES"
        const val EXTRA_LANGUAGE = "com.anishuu.LANGUAGE"
        const val EXTRA_AUTHOR = "com.anishuu.AUTHOR"
        const val EXTRA_PUBLISHER = "com.anishuu.PUBLISHER"
        const val EXTRA_NOTES = "com.anishuu.NOTES"
    }
}
