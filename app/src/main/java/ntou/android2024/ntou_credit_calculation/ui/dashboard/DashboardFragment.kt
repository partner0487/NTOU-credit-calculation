package ntou.android2024.ntou_credit_calculation.ui.dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {isGranted ->
        if (isGranted) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "text/*"
            resultLauncher.launch(intent)
        }
    }
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT < 33) {
                requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val test: Button = binding.test
        test.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "text/*"
            resultLauncher.launch(intent)
        }

        val ntou: ImageView = binding.NTOU
        ntou.setOnClickListener{
            openBrowser(ntou)
        }

        //傳送資料
        val complete: Button = binding.complete
        complete.setOnClickListener{
            val text: TextView = binding.csvText
            if(text.text == "") return@setOnClickListener
            else if(!text.text.contains("資訊工程學系")) {
                val toast = Toast.makeText(context , "目前只限海大資工的學生喔", Toast.LENGTH_SHORT)
                toast.show()
                return@setOnClickListener
            }
            val bundle = Bundle().apply{
                putStringArray("data", str.toTypedArray())
            }

            val toast = Toast.makeText(context , "匯入成功", Toast.LENGTH_SHORT)
            toast.show()
            findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_home,bundle)

            //val fragment = HomeFragment()
            //fragment.arguments = bundle
        }

        return root
    }

    private fun openBrowser(view: View) {

        //Get url from tag
        val url = view.tag as String
        val intent = Intent()
        intent.setAction(Intent.ACTION_VIEW)
        intent.addCategory(Intent.CATEGORY_BROWSABLE)

        //pass the url to intent data
        intent.setData(Uri.parse(url))
        startActivity(intent)
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
                    str = readCSV(uri)
                    text.text = str[0]
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