package com.example.ticktick.util

import androidx.appcompat.widget.SearchView


inline fun SearchView.onQueryTextChanged(crossinline listener : (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true   // we dont want to confirm we want it to happen immediately
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}