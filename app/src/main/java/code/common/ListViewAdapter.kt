package code.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.amtz.v2home.R
import java.util.*


class ListViewAdapter(context: Context, animalNamesList: MutableList<AnimalNames>?) :
    BaseAdapter() {
    // Declare Variables
    var mContext: Context
    var inflater: LayoutInflater
    private var animalNamesList: MutableList<AnimalNames>? = null
    private val arraylist: ArrayList<AnimalNames>

    inner class ViewHolder {
        var name: TextView? = null
    }

    override fun getCount(): Int {
        return animalNamesList!!.size
    }

    override fun getItem(position: Int): AnimalNames {
        return animalNamesList!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var view: View? = view
        val holder: ViewHolder
        if (view == null) {
            holder = ViewHolder()
            view = inflater.inflate(R.layout.search_data_layout, null)
            // Locate the TextViews in listview_item.xml
            if (view != null) {
                holder.name = view.findViewById(R.id.name)
            }
            view.setTag(holder)
        } else {
            holder = view.getTag() as ViewHolder
        }
        // Set the results into TextViews

        holder.name!!.text = animalNamesList!![position].animalName



        return view!!
    }

    // Filter Class
    fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        animalNamesList!!.clear()
        if (charText.length == 0) {
            animalNamesList!!.addAll(arraylist)
        } else {
            for (wp in arraylist) {
                if (wp.animalName.toLowerCase(Locale.getDefault()).contains(charText)) {
                    animalNamesList!!.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }

    init {
        mContext = context
        this.animalNamesList = animalNamesList
        inflater = LayoutInflater.from(mContext)
        arraylist = ArrayList<AnimalNames>()
        arraylist.addAll(animalNamesList!!)
    }
}