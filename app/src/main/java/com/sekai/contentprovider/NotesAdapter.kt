package com.sekai.contentprovider

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sekai.contentprovider.database.NotesDatabaseHelper.Companion.DESCRIPTION_NOTES
import com.sekai.contentprovider.database.NotesDatabaseHelper.Companion.TITLE_NOTES
import kotlinx.android.synthetic.main.note_item.view.*

class NotesAdapter(val listener: NoteClickedListener) : RecyclerView.Adapter<NotesViewHolder>() {

    private var mCursor: Cursor? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder =
        NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        )


    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        mCursor?.moveToPosition(position)

        holder.title.text = mCursor?.getString(mCursor?.getColumnIndex(TITLE_NOTES) as Int)
        holder.descriptions.text = mCursor?.getString(mCursor?.getColumnIndex(DESCRIPTION_NOTES) as Int)

        holder.notebtnRemover.setOnClickListener {
            mCursor?.moveToPosition(position)
            listener.noteRemoveItem(mCursor)
            notifyDataSetChanged()
        }

        holder.itemView.setOnClickListener {
            listener.noteClickItem(mCursor as Cursor)
        }

    }

    override fun getItemCount(): Int = if (mCursor!=null) mCursor?.count as Int else 0

    fun setCursor(newCursor: Cursor?) {
        mCursor = newCursor
        notifyDataSetChanged()

    }
}



class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title = itemView.notes_title
    val descriptions = itemView.notes_description
    val notebtnRemover = itemView.notes_btn_remover

}