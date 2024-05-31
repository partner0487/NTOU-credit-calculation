package ntou.android2024.ntou_credit_calculation.ui.dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ntou.android2024.ntou_credit_calculation.R
import ntou.android2024.ntou_credit_calculation.databinding.FragmentDashboardBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var str: List<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val test: Button = binding.test
        test.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "text/*"
            resultLauncher.launch(intent)
        }

        //傳送資料
        val complete: Button = binding.complete
        complete.setOnClickListener{
            val bundle = Bundle().apply{
                val text: TextView = binding.csvText
                putStringArray("data", str.toTypedArray())
            }
            findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_home,bundle)
        }

        return root
    }

    @SuppressLint("SetTextI18n")
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val text: TextView = binding.csvText
            val data: Intent? = result.data
            if (data != null) {
                val uri: Uri? = data.data
                if(uri != null){
                    text.textSize = 20F
                    val content = ArrayList<String>()
                    str = readCSV(uri)//.joinToString(separator = "\n")
                    text.text = str[0]
                }
                else{
                    text.text = "無效"
                }
            }
        }
    }

    @SuppressLint("Recycle")
    @Throws(IOException::class)
    fun readCSV(uri: Uri): List<String> {
        val csvFile: InputStream? = activity?.contentResolver?.openInputStream(uri)
        val isr = InputStreamReader(csvFile, "Big5")
        return BufferedReader(isr).readLines()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}