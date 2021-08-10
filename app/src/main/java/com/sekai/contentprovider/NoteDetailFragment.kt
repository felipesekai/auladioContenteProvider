package com.sekai.contentprovider

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import androidx.core.content.contentValuesOf
import androidx.fragment.app.DialogFragment
import com.sekai.contentprovider.database.NoteProvider.Companion.URI_NOTES
import com.sekai.contentprovider.database.NotesDatabaseHelper
import com.sekai.contentprovider.database.NotesDatabaseHelper.Companion.DESCRIPTION_NOTES
import com.sekai.contentprovider.database.NotesDatabaseHelper.Companion.TITLE_NOTES
import kotlinx.android.synthetic.main.notes_detail.*

class NoteDetailFragment : DialogFragment(), DialogInterface.OnClickListener {
    lateinit var noteEditTitle : EditText
    lateinit var noteEdtDescription  : EditText
    private var id : Long = 0

    companion object{
        private val EXTRA_ID = "id"
        fun newInstance(id:Long): NoteDetailFragment {
            val bundle = Bundle()
            bundle.putLong(EXTRA_ID, id)
            val notesFragment = NoteDetailFragment()
            notesFragment.arguments = bundle

            return notesFragment
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity?.layoutInflater?.inflate(R.layout.notes_detail, null)
       noteEditTitle = view?.findViewById(R.id.notes_edt_title)!!
       noteEdtDescription = view?.findViewById(R.id.notes_edt_description)!!


        var newNote = true

            if (arguments !=null && arguments?.getLong(EXTRA_ID) != 0L){
                id = arguments?.getLong(EXTRA_ID) as Long
                val uri = Uri.withAppendedPath(URI_NOTES, id.toString())

                var cursor = activity?.contentResolver?.query(uri,null,null,null,null)

                    if (cursor?.moveToNext() as Boolean){
                        newNote = false
                        noteEditTitle.setText(cursor.getString(cursor.getColumnIndex(TITLE_NOTES)))
                        noteEdtDescription.setText(cursor.getString(cursor.getColumnIndex(DESCRIPTION_NOTES)))

                    }
                cursor.close()
            }

        return AlertDialog.Builder(activity as Activity)
            .setTitle(if (newNote) "Nova Mensagem" else "Editar Mensagem")
            .setView(view)
            .setPositiveButton("Salvar", this)
            .setNegativeButton("Cancelar", this)
            .create()
    }
    override fun onClick(dialog: DialogInterface?, which: Int) {
        val values = contentValuesOf()
        values.put(TITLE_NOTES, noteEditTitle.text.toString())
        values.put(DESCRIPTION_NOTES, noteEdtDescription.text.toString())

        if (id != 0L){
            val uri = Uri.withAppendedPath(URI_NOTES, id.toString() )
            context?.contentResolver?.update(uri, values,null,null)
        }else {
            context?.contentResolver?.insert(URI_NOTES, values)
        }
    }

}