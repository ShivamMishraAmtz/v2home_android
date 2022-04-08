package code.common

import TodoAdapter
import android.content.Context
import android.content.res.ColorStateList
import android.opengl.Visibility
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amtz.v2home.R

class TodoSectionAdapter(context: Context, items: ArrayList<TodoSectionModel>?) :
    RecyclerView.Adapter<TodoSectionAdapter.TodoSectionViewHolder>() {
    private val items: ArrayList<TodoSectionModel>?
    private val context: Context

    init {
        this.items = items
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoSectionViewHolder {
        val view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, null)
        return TodoSectionViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (items != null) {
            return items.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: TodoSectionViewHolder, position: Int) {
        val name = items?.get(position)?.date
        val weekname = items?.get(position)?.weekname
        val sections = items?.get(position)?.items
        val adapter = TodoAdapter(context, sections)
        holder.recyclerView.setHasFixedSize(true)
        holder.recyclerView.layoutManager = GridLayoutManager(context, 2)
        holder.recyclerView.adapter = adapter
        holder.title.text = weekname + " " + name + "";

        if (position == (items?.size)!! - 1) {
            holder.vBottom.visibility = View.VISIBLE
        } else {
            holder.vBottom.visibility = View.GONE
        }

    }

    inner class TodoSectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView
        var vBottom: View
        var recyclerView: RecyclerView

        init {
            this.title = view.findViewById(R.id.title) as TextView
            this.vBottom = view.findViewById(R.id.vBottom) as View
            this.recyclerView = view.findViewById(R.id.recyclerViewTodo) as RecyclerView
        }
    }


}