package ntou.android2024.ntou_credit_calculation.ui.home

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ntou.android2024.ntou_credit_calculation.R
import ntou.android2024.ntou_credit_calculation.databinding.FragmentHomeBinding
import java.io.File
import java.io.FileOutputStream

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
        val outputPdf: Button = binding.outputPdf
        val textName = binding.textName
        val numberId = binding.numberId

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
        outputPdf.setOnClickListener{
            //這裡
            val subjectList = mutableListOf(
                subject("國文領域(一)", binding.chinese1.isChecked, 2),
                subject("國文領域(二)", binding.chinese2.isChecked, 2),
                subject("大一英文(上)", binding.english1.isChecked, 2),
                subject("大一英文(下)", binding.english2.isChecked, 2),
                subject("進階英文", binding.english3.isChecked, 2),
                subject("海洋科學概論", binding.ocean.isChecked, 2),
                subject("人工智慧概論", binding.AI.isChecked, 2),
                subject("服務學習 愛校服務(一)", binding.clean1.isChecked, 0),
                subject("服務學習 愛校服務(二)", binding.clean2.isChecked, 0),
                subject("普通物理", binding.physic.isChecked, 3),
                subject("計算機概論", binding.computers.isChecked, 3),
                subject("微積分(上)", binding.calculus1.isChecked, 3),
                subject("微積分(下)", binding.calculus2.isChecked, 3),
                subject("程式設計", binding.programming.isChecked, 3),
                subject("資料結構", binding.dataStructure.isChecked, 3),
                subject("離散數學", binding.discrete.isChecked, 3),
                subject("演算法", binding.algorithm.isChecked, 3),
                subject("數位邏輯", binding.digitalLogic.isChecked, 3),
                subject("數位邏輯實驗", binding.digitalLogicExp.isChecked, 3),
                subject("程式設計(二)", binding.programming2.isChecked, 3),
                subject("計算機組織學", binding.computationalHistology.isChecked, 3),
                subject("線性代數", binding.linearAlgebra.isChecked, 3),
                subject("機率論", binding.probability.isChecked, 3),
                subject("作業系統", binding.os.isChecked, 3),
                subject("電腦網路", binding.network.isChecked, 3),
                subject("資訊專題討論", binding.projectDiscussion.isChecked, 3),
                subject("資工系專題(一)", binding.project1.isChecked, 3),
                subject("資工系專題(二)", binding.project2.isChecked, 3)
            )

            val name = textName.text
            val studentId = numberId.text

            var totalCredit = 0;
            val it = subjectList.iterator()
            while(it.hasNext()){
                val now = it.next()
                if(now.pass)
                    totalCredit += now.credit
                else
                    it.remove()
            }
            val pdfDetails = PdfDetails(name.toString(), studentId.toString(), totalCredit, subjectList)

            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.pdf_layout, null)

            val studentName = view.findViewById<TextView>(R.id.txt_student_name)
            val studentID = view.findViewById<TextView>(R.id.txt_studentId)
            val credit = view.findViewById<TextView>(R.id.txt_total_credit)
            val recyclerView = view.findViewById<RecyclerView>(R.id.pdf_marks)

            val adapter = MarksRecyclerAdapter(pdfDetails.subjectList)     //Object for the recycler view adapter class

            /*Assign values to each view using the data in PDFDetails*/
            studentName.text = pdfDetails.Name
            studentID.text = pdfDetails.studentId
            credit.text = pdfDetails.totalCredit.toString()
            recyclerView.adapter = adapter

            val displayMetrics = DisplayMetrics()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                context?.display?.getRealMetrics(displayMetrics)
                displayMetrics.densityDpi
            }
            else{
                activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            }
            view.measure(
                View.MeasureSpec.makeMeasureSpec(
                    displayMetrics.widthPixels, View.MeasureSpec.EXACTLY
                ),
                View.MeasureSpec.makeMeasureSpec(
                    displayMetrics.heightPixels, View.MeasureSpec.EXACTLY
                )
            )

            view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
            val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            Bitmap.createScaledBitmap(bitmap, 2100, 2970, true)
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(1050, 1485, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            page.canvas.drawBitmap(bitmap, 0F, 0F, null)
            pdfDocument.finishPage(page)
            val filePath = File(context?.getExternalFilesDir(null), "report.pdf")
            pdfDocument.writeTo(FileOutputStream(filePath))
            pdfDocument.close()
        }
        
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
