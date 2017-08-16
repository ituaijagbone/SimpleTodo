package com.ituaijagbone.simpletodo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class EditItemActivity : AppCompatActivity() {
    lateinit var mtEditItem: EditText
    var itemPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)
        val text = intent.getStringExtra(MainActivity.INTENT_EXTRA_TEXT)
        itemPosition = intent.getIntExtra(MainActivity.INTENT_EXTRA_ITEM_POSITION, 0)
        mtEditItem = findViewById(R.id.etEditItem) as EditText
        mtEditItem.setText(text ?: "")
    }

    override fun onResume() {
        super.onResume()
        mtEditItem.showKeyboard()
    }

    override fun onPause() {
        super.onPause()
        mtEditItem.hideKeyboard()
    }

    fun onSave(view: View) {
        val data = Intent()
        data.putExtra(MainActivity.INTENT_EXTRA_TEXT, mtEditItem.text.toString())
        data.putExtra(MainActivity.INTENT_EXTRA_ITEM_POSITION, itemPosition)
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}

fun View.showKeyboard() {
    this.requestFocus()
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}