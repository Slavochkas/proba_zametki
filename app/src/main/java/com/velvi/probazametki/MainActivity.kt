package com.velvi.probazametki

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import java.io.File
import java.io.FileInputStream

class MainActivity : Activity() {

    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val notes = mutableListOf<String>()
    private val storageFile = File(filesDir, "notes.txt")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inputEdit = EditText(this)
        val saveBtn = Button(this)
        saveBtn.text = "Сохранить"

        listView = ListView(this)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notes)
        listView.adapter = adapter

        saveBtn.setOnClickListener {
            val text = inputEdit.text.toString().trim()
            if (text.isNotEmpty()) {
                notes.add(text)
                saveToDisk()
                adapter.notifyDataSetChanged()
                inputEdit.text.clear()
            }
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(inputEdit)
            addView(saveBtn)
            addView(listView)
        }

        setContentView(layout)

        loadFromDisk()
    }

    private fun loadFromDisk() {
        notes.clear()
        if (storageFile.exists()) {
            storageFile.inputStream().use { stream ->
                stream.bufferedReader().use { reader ->
                    reader.readLines().forEach { notes.add(it) }
                }
            }
        }
        adapter.clear()
        adapter.addAll(notes)
        adapter.notifyDataSetChanged()
    }

    private fun saveToDisk() {
        storageFile.outputStream().use { stream ->
            notes.forEach { line -> stream.write((line + "\n").toCharArray()) }
        }
    }
}
