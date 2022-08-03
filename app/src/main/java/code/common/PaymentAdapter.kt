package code.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amtz.v2home.R
import com.kofigyan.stateprogressbar.StateProgressBar

class PaymentAdapter(private val mList: ArrayList<PaymentModal>) :

    RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {
    var descriptionData = arrayOf("Pending", "Cancelled", "Confirm")

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_payment_layout, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]
        holder.textView.text = "Payment Order ID: #" + ItemsViewModel.payment_order_id
        holder.btnStatus.text = ItemsViewModel.get_payment_status_display
        holder.textTransactionNo.text =
            "Package Amount: " + AppConstants.currency + ItemsViewModel.amount
        holder.btnPrice.text = AppConstants.currency + ItemsViewModel.total_amount
        holder.textViewDate.text = "Date: " + ItemsViewModel.payment_date

        if (ItemsViewModel.transaction == "") {
            holder.textViewShortDesc.text = "Transaction ID: N/A"
        } else {
            holder.textViewShortDesc.text = "Transaction ID: " + ItemsViewModel.transaction
        }
        holder.textViewPaymentOrderId.text = "Payment Order ID: " + ItemsViewModel.payment_order_id
        holder.your_state_progress_bar_id.setStateDescriptionData(descriptionData)

        if (ItemsViewModel.get_payment_status_display == "1") {
            holder.your_state_progress_bar_id.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)
        } else if (ItemsViewModel.get_payment_status_display == "2") {
            holder.your_state_progress_bar_id.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
        } else if (ItemsViewModel.get_payment_status_display == "3") {
            holder.your_state_progress_bar_id.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.textViewTitle)
        val textTransactionNo: TextView = itemView.findViewById(R.id.textTransactionNo)
        val textViewShortDesc: TextView = itemView.findViewById(R.id.textViewShortDesc)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        val btnPrice: TextView = itemView.findViewById(R.id.btnPrice)
        val textViewPaymentOrderId: TextView = itemView.findViewById(R.id.textViewPaymentOrderId)
        val your_state_progress_bar_id: StateProgressBar =
            itemView.findViewById(R.id.your_state_progress_bar_id)
        val btnStatus: Button = itemView.findViewById(R.id.btnStatus)
    }
}
