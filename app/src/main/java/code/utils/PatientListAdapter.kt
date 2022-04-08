package code.utils
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import code.common.PatientModal
import code.dashboard.CompounderActivity
import code.dashboard.DashboardActivity
import code.dashboard.LocationActvity
import com.amtz.v2home.R
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.kofigyan.stateprogressbar.StateProgressBar
import org.json.JSONException
import org.json.JSONObject

class PatientListAdapter(private val mList: ArrayList<PatientModal>, private val mActivity: Activity) :


    RecyclerView.Adapter<PatientListAdapter.ViewHolder>() {


    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_patient_list_layout, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]

        holder.tvFname.text =  ItemsViewModel.first_name+" "+ItemsViewModel.last_name;
        holder.tvUnique.text =  "Unique No: "+ItemsViewModel.unique_number;
        holder.btnMobile.text =  "Mobile No: "+ItemsViewModel.mobile_number;
        holder.pincode.text =  "Pin Code: "+ItemsViewModel.pincode;

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvFname: TextView = itemView.findViewById(R.id.tvFname)
        val tvUnique: TextView = itemView.findViewById(R.id.tvUnique)
        val btnMobile: TextView = itemView.findViewById(R.id.btnMobile)
        val pincode: TextView = itemView.findViewById(R.id.pincode)

    }


}
