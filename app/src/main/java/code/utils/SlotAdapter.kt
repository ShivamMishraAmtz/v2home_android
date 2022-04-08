package code.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import code.common.SlotModal
import code.dashboard.LocationActvity
import com.amtz.v2home.R

internal class MainAdapter(
    private val context: Context,
    private val numbersInWords: ArrayList<SlotModal>,
) :
    BaseAdapter() {
    public var old_date = "";
    private var layoutInflater: LayoutInflater? = null
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var textViewHead: TextView
    private lateinit var liMain: LinearLayout
    private lateinit var liBlock: LinearLayout
    override fun getCount(): Int {
        return numbersInWords.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View? {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.slot_items, null)
        }
        textView = convertView!!.findViewById(R.id.textView)
        textViewHead = convertView!!.findViewById(R.id.textViewHead)
        liMain = convertView!!.findViewById(R.id.liMain)
        liBlock = convertView!!.findViewById(R.id.liBlock)


        if (numbersInWords.get(position).start_time == "") {
            liMain.visibility = View.GONE
        } else {
            textView.text =
                numbersInWords.get(position).start_time + " To " + numbersInWords.get(position).end_time;
            textViewHead.text =
                numbersInWords.get(position).day_date + " (" + numbersInWords.get(position).week_name + ")"
        }


        if (old_date == numbersInWords.get(position).day_date && position != 0) {
            textViewHead.visibility = View.INVISIBLE
            old_date = numbersInWords.get(position).day_date;
        } else {
            textViewHead.visibility = View.VISIBLE
            old_date = numbersInWords.get(position).day_date;
        }


        liMain.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {

            }
        })

        return convertView
    }
}