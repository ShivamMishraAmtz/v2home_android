package code.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import code.common.TodoModel
import code.common.TodoSectionAdapter
import code.common.TodoSectionModel
import com.amtz.v2home.R
import com.amtz.v2home.databinding.ActivityDashboardBinding
import com.amtz.v2home.databinding.ActivityExampleBinding

class ExampleActivity : AppCompatActivity() {

    private val items: ArrayList<TodoSectionModel>? = ArrayList()
    private lateinit var binding: ActivityExampleBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

}