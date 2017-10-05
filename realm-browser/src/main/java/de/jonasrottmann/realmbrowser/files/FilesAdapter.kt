package de.jonasrottmann.realmbrowser.files

import android.content.Context
import android.os.Bundle
import android.support.annotation.RestrictTo
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotterknife.bindView

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class FilesAdapter(private val context: Context, private val listener: OnFileSelectedListener) : RecyclerView.Adapter<FilesAdapter.ViewHolder>() {

    private var files: List<FilesPojo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        val viewHolder = ViewHolder(itemView)
        itemView.setOnClickListener { listener.onFileSelected(this.files[viewHolder.adapterPosition]) }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = files[position].name
        holder.subTitle.text = Formatter.formatShortFileSize(context, files[position].sizeInByte)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: List<Any>?) {
        if (payloads == null || payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val o = payloads[0] as Bundle
            for (key in o.keySet()) {
                if (key == FilesDiffUtilsCallback.KEY_NAME) {
                    holder.title.text = o.getString(key)
                } else if (key == FilesDiffUtilsCallback.KEY_SIZE) {
                    holder.subTitle.text = o.getString(key)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return this.files.size
    }

    fun swapList(newList: List<FilesPojo>) {
        val diffResult = DiffUtil.calculateDiff(FilesDiffUtilsCallback(this.files, newList))
        this.files = newList
        diffResult.dispatchUpdatesTo(this)
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView by bindView(android.R.id.text1)
        val subTitle: TextView by bindView(android.R.id.text2)
    }

    interface OnFileSelectedListener {
        fun onFileSelected(file: FilesPojo)
    }
}
