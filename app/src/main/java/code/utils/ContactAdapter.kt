package code.utils

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import code.common.ContactsModal
import code.dashboard.CheckoutActivity
import com.amtz.v2home.R

class ContactAdapter(private val mList: ArrayList<ContactsModal>, private val mActivity: Activity) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_item_layout, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]
        holder.textView.text = "Name: " + ItemsViewModel.customer_name
        holder.textViewMobile.text = "Mobile: " + ItemsViewModel.mobile_number


        if (ItemsViewModel.gender == "1") {
            holder.textViewGender.text = "Gender: Male"
        } else {
            holder.textViewGender.text = "Gender: Female"
        }


        holder.textViewAge.text = "Age: " + ItemsViewModel.age

        holder.textViewDate.text = "Date: " + ItemsViewModel.create_date


        holder.linMain.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {

                holder.linMain.setBackgroundColor(mActivity.resources.getColor(R.color.lightFrey))

                OSettings.putString(AppSettings.contact_id,ItemsViewModel.id);

                mActivity.startActivity(Intent(mActivity, CheckoutActivity::class.java))
                mActivity.finish()
            }
        })

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.textViewName)
        val textViewMobile: TextView = itemView.findViewById(R.id.textViewMobile)
        val textViewGender: TextView = itemView.findViewById(R.id.textViewGender)
        val textViewAge: TextView = itemView.findViewById(R.id.textViewAge)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        val linMain: LinearLayout = itemView.findViewById(R.id.liMain)
    }
}
