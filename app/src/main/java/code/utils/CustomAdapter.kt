package code.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import code.common.PackageModal
import com.amtz.v2home.R
import com.bumptech.glide.load.resource.bitmap.Rotate

class CustomAdapter(private val mList: ArrayList<PackageModal>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]
        // sets the image to the imageview from our itemHolder class
        // sets the text to the textview from our itemHolder class
        holder.textView.text = ItemsViewModel.subcat_name
        holder.textViewDesc.text = ItemsViewModel.sub_category_description
        if (position == mList.size - 1) {
            holder.vLine.visibility = View.GONE;
        } else {
            holder.vLine.visibility = View.GONE;
        }

        holder.liHead.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (holder.liDesc.visibility == View.GONE) {
                    holder.liDesc.visibility = View.VISIBLE
                    holder.ivArrow.rotation=270f
                } else {
                    holder.liDesc.visibility = View.GONE
                    holder.ivArrow.rotation=90f
                }
            }
        })


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val vLine: View = itemView.findViewById(R.id.vLine)
        val liHead: View = itemView.findViewById(R.id.liHead)
        val liDesc: LinearLayout = itemView.findViewById(R.id.liDesc)
        val ivArrow: ImageView = itemView.findViewById(R.id.ivArrow)
        val textViewDesc: TextView = itemView.findViewById(R.id.textViewDesc)
    }
}
