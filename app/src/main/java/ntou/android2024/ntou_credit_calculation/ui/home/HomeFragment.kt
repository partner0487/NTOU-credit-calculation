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
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
        val outputPdf: Button = binding.outputPdfHome
        val inputCsv: Button = binding.outputPdf2

        val r: Resources = resources
        var coreElectiveNum = 0
        var electiveNum = 4000

        //新增核心選修
        addCoreElective.setOnClickListener {
            coreElectiveNum+=2
            val className = r.getString(R.string.core_elective_name)
            val top = R.id.core_elective
            addClass(coreElectiveNum, r, layout, className, top)
        }

        //刪除核心選修
        deleteCoreElective.setOnClickListener {
            if(coreElectiveNum == 0) return@setOnClickListener
            else {
                coreElectiveNum-=2
                val top = R.id.core_elective
                deleteClass(coreElectiveNum, r, layout, top)
            }
        }

        //新增選修
        addElective.setOnClickListener {
            electiveNum+=2
            val className = resources.getString(R.string.elective_name)
            val top = R.id.elective
            addClass(electiveNum, r, layout, className, top)
        }

        //刪除選修
        deleteElective.setOnClickListener {
            if(electiveNum == 0) return@setOnClickListener
            else {
                electiveNum-=2
                val top = R.id.elective
                deleteClass(electiveNum, r, layout, top)
            }
        }

        //傳送資料
        outputPdf.setOnClickListener {
            //傳送資料
            val bundle = Bundle().apply {
                val test: CheckBox = binding.chinese1
                putBoolean("test", test.isChecked)
                putString("test2", test.text.toString())
            }
            findNavController().navigate(
                R.id.action_navigation_home_to_navigation_notifications,
                bundle
            )
        }

        //接收資料
        var countarray = 0
        inputCsv.setOnClickListener {
            val data = arguments?.getStringArray("data")
            val text: TextView = binding.textView
            text.text = data?.get(countarray)
            countarray+=1
        }

        return root
    }


    //新增課程function
    private fun addClass(num :Int, r: Resources, layout: ConstraintLayout, className:String, top:Int){

        var start = 0
        var addId = R.id.add_core_elective
        var deleteId = R.id.delete_core_elective
        if(num>=4000){
            start = 4000
            addId = R.id.add_elective
            deleteId = R.id.delete_elective
        }

        val marginTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8F ,r.displayMetrics).toInt()
        val marginEnd = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20F ,r.displayMetrics).toInt()

        val dynamicCheckBox = CheckBox(context)
        dynamicCheckBox.id = num-1
        dynamicCheckBox.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36F ,r.displayMetrics).toInt()
        dynamicCheckBox.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48F ,r.displayMetrics).toInt()
        layout.addView(dynamicCheckBox)

        val dynamicTextview = EditText(context)
        dynamicTextview.id = num
        dynamicTextview.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130F ,r.displayMetrics).toInt()
        dynamicTextview.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48F ,r.displayMetrics).toInt()
        dynamicTextview.hint = className
        dynamicTextview.textSize = 14F
        layout.addView(dynamicTextview)

        ConstraintSet().apply {
            clone(layout);
            if(num % 4 == 2){
                if(num == start+2){
                    connect(dynamicCheckBox.id, ConstraintSet.TOP, top, ConstraintSet.BOTTOM, marginTop);
                    connect(dynamicTextview.id, ConstraintSet.TOP, top, ConstraintSet.BOTTOM, marginTop);
                }
                else{
                    connect(dynamicCheckBox.id, ConstraintSet.TOP, dynamicCheckBox.id - 4, ConstraintSet.BOTTOM, marginTop);
                    connect(dynamicTextview.id, ConstraintSet.TOP, dynamicTextview.id - 4, ConstraintSet.BOTTOM, marginTop);
                }
                connect(dynamicCheckBox.id, ConstraintSet.LEFT, top, ConstraintSet.LEFT);
                connect(dynamicTextview.id, ConstraintSet.LEFT, dynamicCheckBox.id, ConstraintSet.RIGHT);

                //按鈕往下
                clear(addId, ConstraintSet.TOP)
                connect(addId, ConstraintSet.TOP, dynamicCheckBox.id, ConstraintSet.BOTTOM, marginTop);
                clear(deleteId, ConstraintSet.TOP)
                connect(deleteId, ConstraintSet.TOP, dynamicCheckBox.id, ConstraintSet.BOTTOM, marginTop);

            }
            else{
                if(num == start+4){
                    connect(dynamicCheckBox.id, ConstraintSet.TOP, top, ConstraintSet.BOTTOM, marginTop);
                    connect(dynamicTextview.id, ConstraintSet.TOP, top, ConstraintSet.BOTTOM, marginTop);
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

    //刪除課程function
    private fun deleteClass(num :Int, r: Resources, layout: ConstraintLayout, top:Int){
        var start = 0
        var addId = R.id.add_core_elective
        var deleteId = R.id.delete_core_elective
        if(num>=4000){
            start = 4000
            addId = R.id.add_elective
            deleteId = R.id.delete_elective
        }

        val marginTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8F ,r.displayMetrics).toInt()
        val nowText:TextView = requireView().findViewById(num+2)
        layout.removeView(nowText);
        val nowCheck:TextView = requireView().findViewById(num+1)
        layout.removeView(nowCheck);
        ConstraintSet().apply {
            clone(layout)
            //按鈕往上
            clear(addId, ConstraintSet.TOP)
            clear(deleteId, ConstraintSet.TOP)
            if(num == start){
                connect(addId, ConstraintSet.TOP, top, ConstraintSet.BOTTOM, marginTop);
                connect(deleteId, ConstraintSet.TOP, top, ConstraintSet.BOTTOM, marginTop);
            }
            else{
                connect(addId, ConstraintSet.TOP, num, ConstraintSet.BOTTOM, marginTop);
                connect(deleteId, ConstraintSet.TOP, num, ConstraintSet.BOTTOM, marginTop);
            }
            applyTo(layout);
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
