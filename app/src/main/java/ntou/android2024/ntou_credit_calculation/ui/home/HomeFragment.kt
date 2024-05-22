package ntou.android2024.ntou_credit_calculation.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import ntou.android2024.ntou_credit_calculation.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val layout: ConstraintLayout = binding.layout

        val addCoreElective: Button = binding.addCoreElective
        var coreElectiveNum = 0
        addCoreElective.setOnClickListener {
            coreElectiveNum+=1

            val dynamicCheckBox = CheckBox(context)
            dynamicCheckBox.width = 48
            setMargins(dynamicCheckBox,0,8,0,0)

            val dynamicTextview = EditText(context)
            dynamicTextview.hint = "Dynamically added text"
            dynamicTextview.tag = "@+id/core_elective_$coreElectiveNum"
            setMargins(dynamicTextview,0,8,16,0)

            layout.addView(dynamicCheckBox)
            layout.addView(dynamicTextview)
        }
        /*

        <CheckBox
            android:id="@+id/sport4"
            android:layout_width="48dp"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            app:layout_constraintEnd_toStartOf="@+id/sport4_text1"
            app:layout_constraintTop_toBottomOf="@+id/sport2"
            tools:ignore="DuplicateSpeakableTextCheck,TouchTargetSizeCheck" />

        <EditText
            android:id="@+id/sport4_text1"
            android:layout_width="130dp"
            android:layout_height="48dp"
            android:layout_marginTop="8dp"
            android:layout_marginEnd="16dp"
            android:ems="10"
            android:hint="@string/sport_input"
            android:inputType="text"
            android:textSize="14sp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/sport2_text1"
            tools:ignore="DuplicateSpeakableTextCheck" />
        */

        return root
    }

    fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
        if (v.layoutParams is MarginLayoutParams) {
            val p = v.layoutParams as MarginLayoutParams
            p.setMargins(l, t, r, b)
            v.requestLayout()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}