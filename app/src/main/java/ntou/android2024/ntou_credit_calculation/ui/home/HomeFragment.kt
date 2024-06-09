package ntou.android2024.ntou_credit_calculation.ui.home

import android.annotation.SuppressLint
import android.content.Context
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
import java.io.FileNotFoundException
import java.io.IOException


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
        val arguments = arguments?.getStringArray("data")

        //讀檔
        var txt: Array<String> = emptyArray()
        try {
            val inputStream = requireActivity().openFileInput("save.txt")
            val bytes = ByteArray(inputStream.available())
            val sb = StringBuffer()
            while (inputStream.read(bytes) != -1 && inputStream.read(bytes) != 0) {
                sb.append(String(bytes))
            }
            if (sb.length > 1){
                txt = sb.split(";").toTypedArray()
                if (txt.size > 1) txt = txt.copyOfRange(0, txt.size - 1)
                inputStream.close()
            }
        }
        catch(e: FileNotFoundException) {
            e.printStackTrace()
        }
        catch(e:NumberFormatException) {
            e.printStackTrace()
        }
        catch(e: IOException) {
            e.printStackTrace()
        }
        catch(e:Exception) {
            e.printStackTrace()
        }

        val data: Array<String> = arguments ?: txt //emptyArray()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val layout: ConstraintLayout = binding.layout

        val addCoreElective: Button = binding.addCoreElective
        val deleteCoreElective: Button = binding.deleteCoreElective
        val addElective: Button = binding.addElective
        val deleteElective: Button = binding.deleteElective
        val outputPdf: Button = binding.outputPdfHome

        val r: Resources = resources
        var coreElectiveNum = 0
        var electiveNum = 4000
        val generalClass = arrayOf(
            binding.general1, binding.general2,
            binding.general3, binding.general4,
            binding.general5, binding.general6,
            binding.general7,
        )
        val generalTextClass = arrayOf(
            binding.general1Text, binding.general2Text,
            binding.general3Text, binding.general4Text,
            binding.general5Text, binding.general6Text,
            binding.general7Text,
        )
        val cleanClass = arrayOf(
            binding.clean1, binding.clean2,
        )
        val calculusClass = arrayOf(
            binding.calculus1, binding.calculus2,
        )
        val chineseClass = arrayOf(
            binding.chinese1, binding.chinese2,
        )
        val englishClass = arrayOf(
            binding.english1, binding.english2,
        )
        val requiredClass = arrayOf(
            binding.ocean, binding.AI,
            binding.physic, binding.computers,
            binding.programming, binding.dataStructure,
            binding.discrete, binding.algorithm,
            binding.digitalLogic, binding.digitalLogicExp,
            binding.programming2, binding.computationalHistology,
            binding.linearAlgebra, binding.probability,
            binding.os, binding.network,
            binding.projectDiscussion, binding.project1,
            binding.project2
        )
        val sportClass = arrayOf(
            binding.sport1, binding.sport2,
            binding.sport3, binding.sport4,
        )
        val sportTextClass = arrayOf<EditText>(
            binding.sport1Text, binding.sport2Text,
            binding.sport3Text, binding.sport4Text,
        )
        val coreClass = arrayOf(
            "數位系統", "微處理器原理與組合語言", "計算機系統設計", "計算機結構", "嵌入式系統設計",
            "JAVA程式設計", "程式語言", "資料庫系統", "編譯器", "系統程式", "軟體工程",
            "機器學習技術", "人工智慧", "資訊安全導論"
        )

        //新增核心選修
        addCoreElective.setOnClickListener {
            coreElectiveNum += 2
            val top = R.id.core_elective
            addClass(coreElectiveNum, r, layout, "", top, false)
        }

        //刪除核心選修
        deleteCoreElective.setOnClickListener {
            if (coreElectiveNum == 0) return@setOnClickListener
            else {
                coreElectiveNum -= 2
                val top = R.id.core_elective
                deleteClass(coreElectiveNum, r, layout, top)
            }
        }

        //新增選修
        addElective.setOnClickListener {
            electiveNum += 2
            val top = R.id.elective
            addClass(electiveNum, r, layout, "", top, false)
        }

        //刪除選修
        deleteElective.setOnClickListener {
            if (electiveNum == 4000) return@setOnClickListener
            else {
                electiveNum -= 2
                val top = R.id.elective
                deleteClass(electiveNum, r, layout, top)
            }
        }

        //存檔
        val save: Button = binding.save
        save.setOnClickListener {
            try {
                val filePath = "save.txt"
                val outputStream = requireActivity().openFileOutput(filePath, Context.MODE_PRIVATE)
                if (arguments != null) {
                    for (element in arguments) {
                        outputStream.write(("$element;").toByteArray())
                    }
                }
                outputStream.close()
            }
            catch(e: FileNotFoundException) {
                e.printStackTrace()
            }
            catch(e:NumberFormatException) {
                e.printStackTrace()
            }
            catch(e: IOException) {
                e.printStackTrace()
            }
            catch(e:Exception) {
                e.printStackTrace()
            }
        }

        //傳送資料
        outputPdf.setOnClickListener {
            //傳送資料
            val bundle = Bundle().apply {
                putString("name", binding.name.text.toString())
                putString("id", binding.numberId.text.toString())

                var subNameList = ArrayList<String>()
                var creditList = ArrayList<String>()
                for(i in 0 .. binding.layout.childCount){
                    var child = binding.layout.getChildAt(i)
                    if(child is CheckBox){
                        if(child.isChecked){
                            var nxt = binding.layout.getChildAt(i + 1)
                            if(nxt is EditText){
                                subNameList.add(nxt.text.toString())
                                creditList.add(nxt.tag.toString())
                            }
                            else{
                                subNameList.add(child.text.toString())
                                creditList.add(child.tag.toString())
                            }
                        }
                    }
                }
                putStringArrayList("subName", subNameList)
                putStringArrayList("credit", creditList)
            }
            findNavController().navigate(
                R.id.action_navigation_home_to_navigation_notifications,
                bundle
            )
        }

        //接收資料
        if (data.isNotEmpty()) {
            val dataSize = data.size - 1

            val name = data[0].split(' ')[9].split('-')[1]
            val nameText: TextView = binding.name
            nameText.text = name

            val id = data[0].split(' ')[8].split('-')[1]
            val idText: TextView = binding.numberId
            idText.text = id

            var sportNum = 0
            var calculus = 0
            var chinese = 0
            var english = 0
            var clean = 0
            var general = 0
            var project = 1
            var total = 0
            for (i in 2..dataSize) {
                val dataArray = data[i].replace("\"", "").split(",=")
                val credit = dataArray[3]
                val type = dataArray[4]
                val className = dataArray[5]
                val score = dataArray[11]

                if (type == "必修" || type == "抵") {
                    if (className.contains("游泳") && !binding.swim.isChecked) { //游泳
                        sportTextClass[sportNum].setText(className)
                        sportClass[sportNum].isChecked = true
                        sportNum++
                        binding.swim.isChecked = true
                    } else if (credit == "0" && sportNum <= 3) { //一般體育
                        sportTextClass[sportNum].setText(className)
                        sportClass[sportNum].isChecked = true
                        sportNum++
                    } else { //必修
                        val requiredSize = requiredClass.size - 1
                        for (j in 0..requiredSize) {
                            if (className == requiredClass[j].text) {
                                requiredClass[j].isChecked = true
                                break
                            } else if (className == "微積分") { //微積分
                                calculusClass[calculus].isChecked = true
                                calculus++
                                break
                            } else if (className.contains("國文領域")) { //國文
                                chineseClass[chinese].isChecked = true
                                chinese++
                                break
                            } else if (className.contains("大一英文")) { //英文
                                englishClass[english].isChecked = true
                                english++
                                break
                            }
                        }
                    }
                } else if (type == "服務學習" && clean <= 2) { //服務學習
                    cleanClass[clean].isChecked = true
                    clean++
                } else if ((className.contains("英")) && (type == "選修")) { //服務學習
                    binding.english3.isChecked = true
                } else if (type == "通識" && general <= 6) { //服務學習
                    generalTextClass[general].setText(className)
                    generalClass[general].isChecked = true
                    general++
                } else if (className.contains("資工系專題") && project <= 2) { //服務學習
                    val projectID = requiredClass.size - project
                    requiredClass[projectID].isChecked = true
                    project++
                } else if ((type == "通識") || (type == "選修")) { //服務學習
                    val coreSize = coreClass.size - 1
                    var notCore = true
                    for (j in 0..coreSize) {
                        if (className == coreClass[j]) {
                            coreElectiveNum += 2
                            val top = R.id.core_elective
                            addClass(coreElectiveNum, r, layout, className, top, true)
                            notCore = false
                            break
                        }
                    }
                    if (notCore) {
                        electiveNum += 2
                        val top = R.id.elective
                        addClass(electiveNum, r, layout, className, top, true)
                    }
                }
            }
        }

        return root
    }

    //新增課程function
    private fun addClass(num :Int, r: Resources, layout: ConstraintLayout, className:String, top:Int, tf:Boolean){

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
        dynamicCheckBox.isChecked = tf
        layout.addView(dynamicCheckBox)

        val dynamicTextview = EditText(context)
        dynamicTextview.id = num
        dynamicTextview.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130F ,r.displayMetrics).toInt()
        dynamicTextview.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48F ,r.displayMetrics).toInt()
        dynamicTextview.hint = resources.getString(R.string.core_elective_name)
        dynamicTextview.tag = "0"
        dynamicTextview.setText(className)
        if(num>=4000) dynamicTextview.hint = resources.getString(R.string.elective_name)
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
