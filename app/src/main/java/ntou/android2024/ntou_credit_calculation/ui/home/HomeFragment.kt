package ntou.android2024.ntou_credit_calculation.ui.home

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import ntou.android2024.ntou_credit_calculation.R
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
        val deleteCoreElective: Button = binding.deleteCoreElective
        val addElective: Button = binding.addElective
        val deleteElective: Button = binding.deleteElective

        val r: Resources = resources
        var coreElectiveNum = 0
        var electiveNum = 4000

        //新增核心選修
        addCoreElective.setOnClickListener {

            val marginTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8F ,r.displayMetrics).toInt()
            val marginEnd = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20F ,r.displayMetrics).toInt()

            coreElectiveNum+=1
            val dynamicCheckBox = CheckBox(context)
            dynamicCheckBox.id = coreElectiveNum
            dynamicCheckBox.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36F ,r.displayMetrics).toInt()
            dynamicCheckBox.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48F ,r.displayMetrics).toInt()
            layout.addView(dynamicCheckBox)

            coreElectiveNum+=1
            val dynamicTextview = EditText(context)
            dynamicTextview.id = coreElectiveNum
            dynamicTextview.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130F ,r.displayMetrics).toInt()
            dynamicTextview.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48F ,r.displayMetrics).toInt()
            dynamicTextview.hint = resources.getString(R.string.core_elective_name)
            dynamicTextview.textSize = 14F
            layout.addView(dynamicTextview)

            val constraintSet = ConstraintSet().apply {
                clone(layout)
                if(coreElectiveNum % 4 == 2){
                    if(coreElectiveNum == 2){
                        connect(dynamicCheckBox.id, ConstraintSet.TOP, R.id.core_elective, ConstraintSet.BOTTOM, marginTop)
                        connect(dynamicTextview.id, ConstraintSet.TOP, R.id.core_elective, ConstraintSet.BOTTOM, marginTop)
                    }
                    else{
                        connect(dynamicCheckBox.id, ConstraintSet.TOP, dynamicCheckBox.id - 4, ConstraintSet.BOTTOM, marginTop);
                        connect(dynamicTextview.id, ConstraintSet.TOP, dynamicTextview.id - 4, ConstraintSet.BOTTOM, marginTop);
                    }
                    connect(dynamicCheckBox.id, ConstraintSet.LEFT, R.id.core_elective, ConstraintSet.LEFT)
                    connect(dynamicTextview.id, ConstraintSet.LEFT, dynamicCheckBox.id, ConstraintSet.RIGHT)

                    //按鈕往下
                    clear(R.id.add_core_elective, ConstraintSet.TOP)
                    connect(R.id.add_core_elective, ConstraintSet.TOP, dynamicCheckBox.id, ConstraintSet.BOTTOM, marginTop);
                    clear(R.id.delete_core_elective, ConstraintSet.TOP)
                    connect(R.id.delete_core_elective, ConstraintSet.TOP, dynamicCheckBox.id, ConstraintSet.BOTTOM, marginTop);

                }
                else{
                    if(coreElectiveNum == 4){
                        connect(dynamicCheckBox.id, ConstraintSet.TOP, R.id.core_elective, ConstraintSet.BOTTOM, marginTop);
                        connect(dynamicTextview.id, ConstraintSet.TOP, R.id.core_elective, ConstraintSet.BOTTOM, marginTop);
                    }
                    else{
                        connect(dynamicCheckBox.id, ConstraintSet.TOP, dynamicCheckBox.id - 4, ConstraintSet.BOTTOM, marginTop);
                        connect(dynamicTextview.id, ConstraintSet.TOP, dynamicTextview.id - 4, ConstraintSet.BOTTOM, marginTop);
                    }
                    connect(dynamicTextview.id, ConstraintSet.RIGHT, R.id.layout, ConstraintSet.RIGHT, marginEnd)
                    connect(dynamicCheckBox.id, ConstraintSet.RIGHT, dynamicTextview.id, ConstraintSet.LEFT);

                }
                applyTo(layout);
            }
        }

        //刪除核心選修
        deleteCoreElective.setOnClickListener {
            if(coreElectiveNum == 0) return@setOnClickListener
            val marginTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8F ,r.displayMetrics).toInt()
            super.onDestroyView()
            val constraintSet = ConstraintSet().apply {
                clone(layout)
                //按鈕往上
                if(coreElectiveNum % 4 == 0){
                    clear(R.id.add_core_elective, ConstraintSet.TOP)
                    clear(R.id.delete_core_elective, ConstraintSet.TOP)
                    if(coreElectiveNum == 0){
                        connect(R.id.add_core_elective, ConstraintSet.TOP, R.id.core_elective, ConstraintSet.BOTTOM, marginTop);
                        connect(R.id.delete_core_elective, ConstraintSet.TOP, R.id.core_elective, ConstraintSet.BOTTOM, marginTop);
                    }
                    else{
                        connect(R.id.add_core_elective, ConstraintSet.TOP, coreElectiveNum, ConstraintSet.BOTTOM, marginTop);
                        connect(R.id.delete_core_elective, ConstraintSet.TOP, coreElectiveNum, ConstraintSet.BOTTOM, marginTop);
                    }
                }
                //applyTo(layout);
            }
        }

        //新增選修
        addElective.setOnClickListener {

            val marginTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8F ,r.displayMetrics).toInt()
            val marginEnd = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20F ,r.displayMetrics).toInt()

            electiveNum+=1
            val dynamicCheckBox = CheckBox(context)
            dynamicCheckBox.id = electiveNum
            dynamicCheckBox.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36F ,r.displayMetrics).toInt()
            dynamicCheckBox.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48F ,r.displayMetrics).toInt()
            layout.addView(dynamicCheckBox)

            electiveNum+=1
            val dynamicTextview = EditText(context)
            dynamicTextview.id = electiveNum
            dynamicTextview.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130F ,r.displayMetrics).toInt()
            dynamicTextview.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48F ,r.displayMetrics).toInt()
            dynamicTextview.hint = resources.getString(R.string.elective_name)
            dynamicTextview.textSize = 14F
            layout.addView(dynamicTextview)

            val constraintSet = ConstraintSet().apply {
                clone(layout);
                if(electiveNum % 4 == 2){
                    if(electiveNum == 4002){
                        connect(dynamicCheckBox.id, ConstraintSet.TOP, R.id.elective, ConstraintSet.BOTTOM, marginTop);
                        connect(dynamicTextview.id, ConstraintSet.TOP, R.id.elective, ConstraintSet.BOTTOM, marginTop);
                    }
                    else{
                        connect(dynamicCheckBox.id, ConstraintSet.TOP, dynamicCheckBox.id - 4, ConstraintSet.BOTTOM, marginTop);
                        connect(dynamicTextview.id, ConstraintSet.TOP, dynamicTextview.id - 4, ConstraintSet.BOTTOM, marginTop);
                    }
                    connect(dynamicCheckBox.id, ConstraintSet.LEFT, R.id.elective, ConstraintSet.LEFT);
                    connect(dynamicTextview.id, ConstraintSet.LEFT, dynamicCheckBox.id, ConstraintSet.RIGHT);

                    //按鈕往下
                    clear(R.id.add_elective, ConstraintSet.TOP)
                    connect(R.id.add_elective, ConstraintSet.TOP, dynamicCheckBox.id, ConstraintSet.BOTTOM, marginTop);
                    clear(R.id.delete_elective, ConstraintSet.TOP)
                    connect(R.id.delete_elective, ConstraintSet.TOP, dynamicCheckBox.id, ConstraintSet.BOTTOM, marginTop);

                }
                else{
                    if(electiveNum == 4004){
                        connect(dynamicCheckBox.id, ConstraintSet.TOP, R.id.elective, ConstraintSet.BOTTOM, marginTop);
                        connect(dynamicTextview.id, ConstraintSet.TOP, R.id.elective, ConstraintSet.BOTTOM, marginTop);
                    }
                    else{
                        connect(dynamicCheckBox.id, ConstraintSet.TOP, dynamicCheckBox.id - 4, ConstraintSet.BOTTOM, marginTop);
                        connect(dynamicTextview.id, ConstraintSet.TOP, dynamicTextview.id - 4, ConstraintSet.BOTTOM, marginTop);
                    }
                    connect(dynamicTextview.id, ConstraintSet.RIGHT, R.id.layout, ConstraintSet.RIGHT, marginEnd)
                    connect(dynamicCheckBox.id, ConstraintSet.RIGHT, dynamicTextview.id, ConstraintSet.LEFT);

                }
                applyTo(layout);
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}