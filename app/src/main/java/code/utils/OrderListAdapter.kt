package code.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import code.basic.SignUpActivity
import code.common.OrderModal
import code.dashboard.CompounderActivity
import code.dashboard.DashboardActivity
import code.dashboard.LocationActvity
import code.dashboard.OrderDetailActivity
import com.amtz.v2home.R
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.kofigyan.stateprogressbar.StateProgressBar
import org.json.JSONException
import org.json.JSONObject

class OrderListAdapter(private val mList: ArrayList<OrderModal>, private val mActivity: Activity) :

    RecyclerView.Adapter<OrderListAdapter.ViewHolder>(), Filterable {

    var descriptionData = arrayOf("Ready to test", "Completed", "Report")

    var mListFiltered: ArrayList<OrderModal> = mList

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_list_layout, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]
        holder.textView.text = "Order Number: #" + ItemsViewModel.order_number
        holder.btnStatus.text = ItemsViewModel.get_order_status_display
        holder.your_state_progress_bar_id.setStateDescriptionData(descriptionData)

        holder.textViewShortDesc.text = ItemsViewModel.customer_name
        holder.textViewUnique.text = "Unique Number: " + ItemsViewModel.unique_number
        holder.btnPrice.text =
            "Gender: " + ItemsViewModel.gender

        if (ItemsViewModel.gender == "Male") {
            holder.ivGender.setImageDrawable(mActivity.resources.getDrawable(R.drawable.ic_male));
        } else {
            holder.ivGender.setImageDrawable(mActivity.resources.getDrawable(R.drawable.ic_female));
        }

        holder.btnPrice.visibility = View.GONE;

        holder.btnMobile.text =
            "Mobile No: " + ItemsViewModel.mobile_number
        holder.btnTime.text = ItemsViewModel.start_time + " To " + ItemsViewModel.end_time;

        holder.btnMobile.visibility = View.GONE;

        if (ItemsViewModel.order_status == "2") {
            holder.btnStatus.text = "Ready For Test"
        } else if (ItemsViewModel.order_status == "4") {
            holder.btnStatus.text = "Completed"
        } else if (ItemsViewModel.order_status == "3") {
            holder.btnStatus.text = "Report Generated"
        }


        if (ItemsViewModel.order_status == "2") {
            holder.your_state_progress_bar_id.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)
        } else if (ItemsViewModel.order_status == "3") {
            holder.your_state_progress_bar_id.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
            holder.your_state_progress_bar_id.setAllStatesCompleted(true)
        } else if (ItemsViewModel.order_status == "4") {
            holder.your_state_progress_bar_id.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
        }

        holder.btnStatus.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (ItemsViewModel.order_status == "2") {
                    hitUpdateApi(ItemsViewModel.id, "4")
                } else if (ItemsViewModel.order_status == "4") {
                    hitUpdateApi(ItemsViewModel.id, "3")
                }
            }
        })


        holder.cvMain.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {

                val i=Intent(mActivity, OrderDetailActivity::class.java)
                i.putExtra("id",ItemsViewModel.id)
                i.putExtra("order_number",ItemsViewModel.order_number)
                i.putExtra("unique_number",ItemsViewModel.unique_number)
                i.putExtra("customer_name",ItemsViewModel.customer_name)
                i.putExtra("gender",ItemsViewModel.gender)
                i.putExtra("mobile_number",ItemsViewModel.mobile_number)
                i.putExtra("age",ItemsViewModel.age)
                i.putExtra("start_time",ItemsViewModel.start_time)
                i.putExtra("end_time",ItemsViewModel.end_time)
                i.putExtra("order_status",ItemsViewModel.order_status)
                i.putExtra("customer_id",ItemsViewModel.customer_id)
                i.putExtra("package_amount",ItemsViewModel.package_amount)
                i.putExtra("package_tax",ItemsViewModel.package_tax)
                i.putExtra("total_amount",ItemsViewModel.total_amount)
                i.putExtra("get_order_status_display",ItemsViewModel.get_order_status_display)
                i.putExtra("order_date_time",ItemsViewModel.order_date_time)
                mActivity.startActivity(i)

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
        val btnStatus: Button = itemView.findViewById(R.id.btnStatus)
        val textViewShortDesc: TextView = itemView.findViewById(R.id.textViewShortDesc)
        val btnPrice: TextView = itemView.findViewById(R.id.btnPrice)
        val btnMobile: TextView = itemView.findViewById(R.id.btnMobile)
        val btnTime: TextView = itemView.findViewById(R.id.btnTime)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        val ivGender: ImageView = itemView.findViewById(R.id.ivGender)
        val textViewUnique: TextView = itemView.findViewById(R.id.textViewUnique)
        val cvMain: CardView = itemView.findViewById(R.id.cvMain)
        val your_state_progress_bar_id: StateProgressBar =
            itemView.findViewById(R.id.your_state_progress_bar_id)
    }

    private fun hitUpdateApi(req_id: String, status: String) {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.post(AppUrls.order_status)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("auth", OSettings.getString("token"))
                .addBodyParameter("orde_request_id", req_id)
                .addBodyParameter("order_status", status)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        AppUtils.hideDialog()
                        Log.v("yurwe", response.toString());
                        parseJson(response);
                    }

                    override fun onError(anError: ANError) {
                        AppUtils.hideDialog()
                        Log.v("yurwe", anError.message.toString());
                    }
                })
        }
    }

    private fun parseJson(response: JSONObject?) {
        Log.v("dsfdasf", response.toString())
        try {
            if (response?.getString(AppConstants.resCode) == "200") {
                Toast.makeText(mActivity, "Status Updated Successfully!", Toast.LENGTH_LONG).show()
                mActivity.startActivity(Intent(mActivity, CompounderActivity::class.java))
                mActivity.finish()
            } else {
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.v("gfds", e.message.toString());

        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty())
                    mListFiltered = mList
                else {
                    val filteredList = ArrayList<OrderModal>()
                    mList
                        .filter {
                            (it.id.contains(constraint!!)) or
                                    (it.order_number.contains(constraint))
                        }
                        .forEach { filteredList.add(it) }
                    mListFiltered = filteredList
                }
                return FilterResults().apply { values = mListFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mListFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<OrderModal>
                notifyDataSetChanged()
            }
        }
    }

}
