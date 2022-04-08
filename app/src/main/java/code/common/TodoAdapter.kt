import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import code.basic.LoginActivity
import code.common.ItemListener
import code.common.SlotModal
import code.utils.AppSettings
import code.utils.OSettings
import com.amtz.v2home.R

class TodoAdapter(context: Context, items: ArrayList<SlotModal>?) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    private val items: ArrayList<SlotModal>?
    private val context: Context
    private var lastPos = 0;

    var itemListener: ItemListener? = null


    init {
        this.items = items
        this.context = context
        itemListener = context as ItemListener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_items, null)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (items != null) {
            return items.size
        }
        return 0
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {

        val title = items?.get(position)?.start_time
        val end_title = items?.get(position)?.end_time
        val week_name = items?.get(position)?.week_name
        holder.todoTitle.text = title + " To " + end_title;

        if (items?.get(position)?.day_slot_id!!.toInt() == OSettings.getInt(AppSettings.position)
            && items?.get(position)?.day_slot_id!!.toInt() != -1
        ) {
            holder.liBlock.setBackgroundTintList(
                ColorStateList.valueOf(
                    context.resources.getColor(
                        R.color.teal_700
                    )
                )
            );
            holder.todoTitle.setTextColor(
                ColorStateList.valueOf(
                    context.resources.getColor(
                        R.color.white
                    )
                )
            );

            OSettings.putString(AppSettings.slot_id,  items?.get(position)?.day_slot_id)
            OSettings.putString(AppSettings.booking_date,  items?.get(position)?.day_date)
            OSettings.putString(AppSettings.week_id,  items?.get(position)?.week_id)
            OSettings.putString(AppSettings.time_from,  items?.get(position)?.start_time)
            OSettings.putString(AppSettings.time_to,  items?.get(position)?.end_time)

            Log.v("gbfds",OSettings.getString(AppSettings.week_id)!!)


        } else {
            holder.liBlock.setBackgroundDrawable(context.resources.getDrawable(R.drawable.rectangle_radious))
            holder.todoTitle.setTextColor(
                ColorStateList.valueOf(
                    context.resources.getColor(
                        R.color.black
                    )
                )
            );
        }

        holder.liBlock.setOnClickListener(object : View.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onClick(view: View?) {
                    itemListener?.onRemove(items?.get(position)?.day_slot_id?.toInt())
            }
        })
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var todoTitle: TextView
        var liBlock: LinearLayout
        init {
            this.todoTitle = view.findViewById(R.id.textView) as TextView
            this.liBlock = view.findViewById(R.id.liBlock) as LinearLayout
        }
    }

}

