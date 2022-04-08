package code.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import code.common.OrderModal
import com.amtz.v2home.R
import com.kofigyan.stateprogressbar.StateProgressBar

class OrderAdapter(private val mList: ArrayList<OrderModal>) :


    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    var descriptionData = arrayOf("Pending", "Ready to test", "Completed", "Report")


    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_list, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]
        holder.your_state_progress_bar_id.setStateDescriptionData(descriptionData)
        holder.textView.text = "Order Number: #" + ItemsViewModel.order_number
        holder.btnStatus.text = ItemsViewModel.get_order_status_display
        holder.textViewShortDesc.text =
            "Package Amount: " + AppConstants.currency + ItemsViewModel.package_amount
        holder.btnPrice.text =
            "Total Amount: " + AppConstants.currency + ItemsViewModel.total_amount

        holder.textViewDate.text = "Order Date: "+ItemsViewModel.order_date_time


        if (ItemsViewModel.get_order_status_display == "1") {
            holder.your_state_progress_bar_id.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)
        } else if (ItemsViewModel.get_order_status_display == "2") {
            holder.your_state_progress_bar_id.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
        } else if (ItemsViewModel.get_order_status_display == "3") {
            holder.your_state_progress_bar_id.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)
        } else if (ItemsViewModel.get_order_status_display == "4") {
            holder.your_state_progress_bar_id.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.textViewTitle)
        val btnStatus: Button = itemView.findViewById(R.id.btnStatus)
        val textViewShortDesc: TextView = itemView.findViewById(R.id.textViewShortDesc)
        val btnPrice: TextView = itemView.findViewById(R.id.btnPrice)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        val your_state_progress_bar_id: StateProgressBar =
            itemView.findViewById(R.id.your_state_progress_bar_id)
    }
}
