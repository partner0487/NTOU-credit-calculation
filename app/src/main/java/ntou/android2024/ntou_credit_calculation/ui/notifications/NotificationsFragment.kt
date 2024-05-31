package ntou.android2024.ntou_credit_calculation.ui.notifications

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ntou.android2024.ntou_credit_calculation.databinding.FragmentNotificationsBinding


class NotificationsFragment : Fragment()  {

    private var _binding: FragmentNotificationsBinding? = null

    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val textView: TextView = binding.textNotifications

        val outputPdf: Button = binding.outputPdf
        outputPdf.setOnClickListener{
            //這裡
            textView.text = arguments?.getBoolean("test").toString() + arguments?.getString("test2")
        }

        val back: Button = binding.back
        back.setOnClickListener{
            //這裡
            findNavController().popBackStack()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}