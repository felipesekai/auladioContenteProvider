package com.sekai.contentprovider

import android.database.Cursor

interface NoteClickedListener {
    fun noteClickItem(cursor: Cursor)
    fun noteRemoveItem(cursor: Cursor?)
}