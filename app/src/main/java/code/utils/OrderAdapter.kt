package code.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import code.common.OrderModal
import com.amtz.v2home.R
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.kofigyan.stateprogressbar.StateProgressBar
import org.json.JSONObject

class OrderAdapter(private val mList: ArrayList<OrderModal>, private val mActivity: Activity) :
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
//        holder.btnStatus.text = ItemsViewModel.get_order_status_display
        holder.textViewShortDesc.text =
            "Package Amount: " + AppConstants.currency + ItemsViewModel.package_amount
        holder.btnPrice.text =
            "Total Amount: " + AppConstants.currency + ItemsViewModel.total_amount

        holder.textViewDate.text = "Order Date: " + ItemsViewModel.order_date_time

        if (ItemsViewModel.get_order_status_display == "1") {
            holder.your_state_progress_bar_id.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)
            holder.btnStatus.visibility = View.GONE
        } else if (ItemsViewModel.get_order_status_display == "2") {
            holder.your_state_progress_bar_id.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
            holder.btnStatus.visibility = View.GONE
        } else if (ItemsViewModel.get_order_status_display == "3") {
            holder.your_state_progress_bar_id.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)
            holder.btnStatus.visibility = View.VISIBLE
        } else if (ItemsViewModel.get_order_status_display == "4") {
            holder.your_state_progress_bar_id.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
            holder.btnStatus.visibility = View.GONE
        }

        //holder.btnStatus.visibility=View.VISIBLE
        holder.btnStatus.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                downLoadApi(ItemsViewModel.id)
            }
        })

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.textViewTitle)
        val btnStatus: ImageView = itemView.findViewById(R.id.btnStatus)
        val textViewShortDesc: TextView = itemView.findViewById(R.id.textViewShortDesc)
        val btnPrice: TextView = itemView.findViewById(R.id.btnPrice)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        val your_state_progress_bar_id: StateProgressBar =
            itemView.findViewById(R.id.your_state_progress_bar_id)
    }

    fun downLoadApi(id:String) {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            Log.v("hgdfsa", OSettings.getString("token").toString())
            AndroidNetworking.post(AppUrls.user_view_report)
                .addHeaders("auth", OSettings.getString("token"))
                .addBodyParameter("order_id", id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        AppUtils.hideDialog()
                        Log.v("jgmhnfgbdvsc", response.toString());
                        if (response.getString(AppConstants.resCode) == "200") {
                            val jsonObject = response.getJSONObject("result");
                            val report = jsonObject.getString("report");
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(AppUrls.baseUrlForImage + report)
                            )
                            mActivity.startActivity(browserIntent)
                        }
                    }
                    override fun onError(anError: ANError) {
                        AppUtils.hideDialog()
                        Log.v("jgmhnfgbdvsc", anError.getErrorBody().toString());
                    }
                })
        }
    }

}
