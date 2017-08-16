package com.ituaijagbone.simpletodo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import java.io.File
import java.io.IOException

val REQUEST_CODE = 20

class MainActivity : AppCompatActivity() {
    var items: ArrayList<String> = arrayListOf()
    lateinit var itemsAdapter: ArrayAdapter<String>
    lateinit var lvItems: ListView
    companion object {
        val INTENT_EXTRA_TEXT = "text"
        val INTENT_EXTRA_ITEM_POSITION = "pos"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lvItems = findViewById(R.id.lvItems) as ListView
        readItems()
        itemsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        lvItems.adapter = itemsAdapter
        lvItems.setOnItemLongClickListener { _, _, pos, _ ->
            items.removeAt(pos)
            itemsAdapter.notifyDataSetChanged()
            writeItems()
            true
        }
        lvItems.setOnItemClickListener { _, _, pos, _ ->
            val intent = Intent(this, EditItemActivity::class.java)
            intent.putExtra(INTENT_EXTRA_ITEM_POSITION, pos)
            intent.putExtra(INTENT_EXTRA_TEXT, items[pos])
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    fun onAddItem(view: View) {
        val etNewItem = findViewById(R.id.etNewItem) as EditText
        val itemText = etNewItem.text.toString()
        if (itemText.isBlank()) return
        itemsAdapter.add(itemText)
        etNewItem.setText("")
        writeItems()
    }

    private fun readItems() {
        val filesDir = filesDir
        val todoFile = File(filesDir, "todo.txt")
        items = try {
            todoFile.readLines().toCollection(items)
        } catch (e: IOException) {
            arrayListOf()
        }
    }

    private fun writeItems() {
        val filesDir = filesDir
        val todoFile = File(filesDir, "todo.txt")
        try {
            todoFile.writeText("")
            items.forEach { todoFile.appendText("$it\n") }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val position = data?.getIntExtra(INTENT_EXTRA_ITEM_POSITION, 0) ?: 0
            data?.getStringExtra(INTENT_EXTRA_TEXT)?.let {
                if (it.isBlank()) return
                items[position] = it
                itemsAdapter.notifyDataSetChanged()
                writeItems()
            }
        }
    }
}
