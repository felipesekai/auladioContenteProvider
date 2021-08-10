package com.sekai.contentprovider

import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns._ID
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sekai.contentprovider.database.NoteProvider.Companion.URI_NOTES
import com.sekai.contentprovider.database.NotesDatabaseHelper.Companion.TITLE_NOTES

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    lateinit var notesRecyclerView: RecyclerView
    lateinit var noteAdd : FloatingActionButton
    lateinit var notesAdapter : NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         noteAdd = findViewById(R.id.note_add)
        noteAdd.setOnClickListener{
            NoteDetailFragment().show(supportFragmentManager, "dialog")
        }

        notesAdapter = NotesAdapter(object : NoteClickedListener{
            override fun noteClickItem(cursor: Cursor) {
                val id = cursor.getLong(cursor.getColumnIndex(_ID))
                val fragment = NoteDetailFragment.newInstance(id)
                fragment.show(supportFragmentManager, "dialog")
            }

            override fun noteRemoveItem(cursor: Cursor?) {
                val id : Long? = cursor?.getLong(cursor?.getColumnIndex(_ID))
                contentResolver.delete(Uri.withAppendedPath(URI_NOTES,id.toString()),null,null)
            }

        }


        )
        notesAdapter.setHasStableIds(true)

        notesRecyclerView = findViewById(R.id.notes_recycle)
        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        notesRecyclerView.adapter = notesAdapter

        LoaderManager.getInstance(this).initLoader(0,null,this)

    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> =
        CursorLoader(this, URI_NOTES, null, null,null, TITLE_NOTES)


    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
       if (data !=null){
            notesAdapter.setCursor(data)
       }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        notesAdapter.setCursor(null)
    }
}