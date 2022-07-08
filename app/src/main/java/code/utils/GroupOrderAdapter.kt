package code.utils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import code.common.GroupOrderModal
import com.amtz.v2home.R
import com.kofigyan.stateprogressbar.StateProgressBar

class GroupOrderAdapter(private val mList: ArrayList<GroupOrderModal>) :


    RecyclerView.Adapter<GroupOrderAdapter.ViewHolder>() {

    var descriptionData = arrayOf("Pending", "Ready to test", "Completed", "Report")


    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.group_item_order_list, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]
        holder.your_state_progress_bar_id.setStateDescriptionData(descriptionData)
        holder.textView.text = "Subject: " + ItemsViewModel.subject
        holder.btnStatus.text = ItemsViewModel.status
        holder.textViewShortDesc.text =
            "Message: " + ItemsViewModel.message
        holder.btnContactNo.text =
            "Contact No: " + ItemsViewModel.contact_number
        //holder.textViewDate.text = "Approved Date: " + ItemsViewModel.approved_check_up_date
        if (ItemsViewModel.status == "1") {
            holder.your_state_progress_bar_id.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)
        } else if (ItemsViewModel.status == "2") {
            holder.your_state_progress_bar_id.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
        } else if (ItemsViewModel.status == "3") {
            holder.your_state_progress_bar_id.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)
        } else if (ItemsViewModel.status == "4") {
            holder.your_state_progress_bar_id.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.textViewSubject)
        val btnStatus: Button = itemView.findViewById(R.id.btnStatus)
        val textViewShortDesc: TextView = itemView.findViewById(R.id.textViewShortDesc)
        val btnContactNo: TextView = itemView.findViewById(R.id.btnContactNo)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        val your_state_progress_bar_id: StateProgressBar =
            itemView.findViewById(R.id.your_state_progress_bar_id)
    }
}
