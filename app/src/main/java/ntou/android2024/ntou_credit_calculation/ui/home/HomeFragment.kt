package ntou.android2024.ntou_credit_calculation.ui.home

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import android.widget.ImageButton
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

    private var totalCredit = 0

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
        val sportTextClass = arrayOf(
            binding.sport1Text, binding.sport2Text,
            binding.sport3Text, binding.sport4Text,
        )
        val coreClass = arrayOf(
            "數位系統", "微處理器原理與組合語言", "計算機系統設計", "計算機結構", "嵌入式系統設計",
            "JAVA程式設計", "程式語言", "資料庫系統", "編譯器", "系統程式", "軟體工程",
            "機器學習技術", "人工智慧", "資訊安全導論"
        )
        val total = binding.total

        //新增核心選修
        addCoreElective.setOnClickListener {
            val lunch: List<String> = listOf("1","2","3","4","5","6")
            var singleChoiceIndex = 0
            AlertDialog.Builder(context)
                .setTitle("學分：")
                .setSingleChoiceItems(lunch.toTypedArray(), singleChoiceIndex) { _, which ->
                    singleChoiceIndex = which
                }
                .setPositiveButton("確認") { dialog, _ ->
                    coreElectiveNum += 2
                    val top = R.id.core_elective
                    addClass(coreElectiveNum, r, layout, "", top, false, lunch[singleChoiceIndex], total)
                    dialog.dismiss()
                }
                .show()
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
            val lunch: List<String> = listOf("1","2","3","4","5","6")
            var singleChoiceIndex = 0
            AlertDialog.Builder(context)
                .setTitle("學分：")
                .setSingleChoiceItems(lunch.toTypedArray(), singleChoiceIndex) { _, which ->
                    singleChoiceIndex = which
                }
                .setPositiveButton("確認") { dialog, _ ->
                    electiveNum += 2
                    val top = R.id.elective
                    addClass(electiveNum, r, layout, "", top, false, lunch[singleChoiceIndex], total)
                    dialog.dismiss()
                }
                .show()
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

        for(i in 0 .. binding.layout.childCount){
            val child = binding.layout.getChildAt(i)
            if(child is CheckBox){
                child.setOnClickListener{
                    if(!child.isChecked){
                        val nxt = binding.layout.getChildAt(i + 1)
                        if(nxt is EditText){
                            totalCredit -= nxt.tag.toString().toInt() - ("0").toInt()
                            total.text = resources.getString(R.string.total) + totalCredit.toString()
                        }
                        else{
                            totalCredit -= child.tag.toString().toInt() - ("0").toInt()
                            total.text = resources.getString(R.string.total) + totalCredit.toString()
                        }
                    }
                    else{
                        val nxt = binding.layout.getChildAt(i + 1)
                        if(nxt is EditText){
                            totalCredit += nxt.tag.toString().toInt() - ("0").toInt()
                            total.text = resources.getString(R.string.total) + totalCredit.toString()
                        }
                        else{
                            totalCredit += child.tag.toString().toInt() - ("0").toInt()
                            total.text = resources.getString(R.string.total) + totalCredit.toString()
                        }
                    }
                }
            }
        }

        //清空
        val trash: ImageButton = binding.trash
        trash.setOnClickListener {
            AlertDialog.Builder(context).setMessage("確定要清空資料嗎")
                .setTitle("清空資料")
                .setPositiveButton("確定"){ _, _ ->
                    try {
                        val filePath = "save.txt"
                        val outputStream = requireActivity().openFileOutput(filePath, Context.MODE_PRIVATE)
                        outputStream.write(("").toByteArray())
                        outputStream.close()

                        findNavController().navigate(
                            R.id.navigation_home
                        )
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
                .setNeutralButton("取消", null)
                .show()
        }

        //存檔
        val save: ImageButton = binding.save
        save.setOnClickListener {
            try {
                val filePath = "save.txt"
                val outputStream = requireActivity().openFileOutput(filePath, Context.MODE_PRIVATE)

                var saveData = emptyArray<String>()
                saveData += "程式代碼：GRD5010, 查詢條件 學制-日間學制 部別-大學部 學院-電機資訊學院 系所-資訊工程學系 年級-3 班級-A " + "學號-" + binding.numberId.text.toString() + " 姓名-" + binding.name.text.toString() + "\n"
                saveData += "學年期,課號,開課班別,學分數,選別,課程名稱,教師姓名,期中評量,期中扣考,期末評量,期末扣考,學期總成績\n"
                for(i in 0 .. binding.layout.childCount){
                    val child = binding.layout.getChildAt(i)
                    if(child is CheckBox){
                        if(child.isChecked){
                            val nxt = binding.layout.getChildAt(i + 1)
                            saveData += if(nxt is EditText){
                                if(nxt.hint.toString().contains("體育")) "=\"\",=\"\",=\"\",=\"" + nxt.tag.toString() + "\",=\"必修\",=\"" + nxt.text.toString() + "\",=\"\",=\"\",=\"\",=\"\",=\"\",=\"\"\n"
                                else if(nxt.hint.toString().contains("博雅")) "=\"\",=\"\",=\"\",=\"" + nxt.tag.toString() + "\",=\"通識\",=\"" + nxt.text.toString() + "\",=\"\",=\"\",=\"\",=\"\",=\"\",=\"\"\n"
                                else "=\"\",=\"\",=\"\",=\"" + nxt.tag.toString() + "\",=\"選修\",=\"" + nxt.text.toString() + "\",=\"\",=\"\",=\"\",=\"\",=\"\",=\"\"\n"
                            } else{
                                if(child.text.toString().contains("服務學習")) "=\"\",=\"\",=\"\",=\"" + child.tag.toString() + "\",=\"服務學習\",=\"" + child.text.toString() + "\",=\"\",=\"\",=\"\",=\"\",=\"\",=\"\"\n"
                                else if(child.text.toString().contains("進階英文")) "=\"\",=\"\",=\"\",=\"" + child.tag.toString() + "\",=\"選修\",=\"" + child.text.toString() + "\",=\"\",=\"\",=\"\",=\"\",=\"\",=\"\"\n"
                                else "=\"\",=\"\",=\"\",=\"" + child.tag.toString() + "\",=\"必修\",=\""+ child.text.toString() +"\",=\"\",=\"\",=\"\",=\"\",=\"\",=\"\"\n"
                            }
                        }
                    }
                }

                for (element in saveData) {
                    outputStream.write(("$element;").toByteArray())
                }
                outputStream.close()

                findNavController().navigate(
                    R.id.action_navigation_home_to_navigation_notifications
                )
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
            for (i in 2..dataSize) {
                val dataArray = data[i].replace("\"", "").split(",=")
                val credit = dataArray[3]
                val type = dataArray[4]
                val className = dataArray[5]
                val score = dataArray[11]
                if(!score.contains("*") && !score.contains("W") && score!=""){
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
                                } else if (className.contains("微積分")) { //微積分
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
                                addClass(coreElectiveNum, r, layout, className, top, true, credit, total)
                                notCore = false
                                break
                            }
                        }
                        if (notCore) {
                            electiveNum += 2
                            val top = R.id.elective
                            addClass(electiveNum, r, layout, className, top, true, credit, total)
                        }
                    }
                    if (credit != "") {
                        totalCredit += credit.toInt() - ("0").toInt()
                    }
                }
            }
            total.text = resources.getString(R.string.total) + totalCredit.toString()
        }
        else{
            total.text = resources.getString(R.string.total) + "0"
        }

        return root
    }

    //新增課程function
    @SuppressLint("SetTextI18n")
    private fun addClass(num :Int, r: Resources, layout: ConstraintLayout, className:String, top:Int, tf:Boolean, credit:String, total:TextView) {

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
        dynamicTextview.tag = credit
        dynamicTextview.setText(className)
        if(num>=4000) dynamicTextview.hint = resources.getString(R.string.elective_name)
        dynamicTextview.textSize = 14F
        layout.addView(dynamicTextview)

        dynamicCheckBox.setOnClickListener{
            if(!dynamicCheckBox.isChecked){
                totalCredit -= dynamicTextview.tag.toString().toInt() - ("0").toInt()
                total.text = resources.getString(R.string.total) + totalCredit.toString()
            }
            else{
                totalCredit += dynamicTextview.tag.toString().toInt() - ("0").toInt()
                total.text = resources.getString(R.string.total) + totalCredit.toString()
            }
        }

        ConstraintSet().apply {
            clone(layout)
            if(num % 4 == 2){
                if(num == start+2){
                    connect(dynamicCheckBox.id, ConstraintSet.TOP, top, ConstraintSet.BOTTOM, marginTop)
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
    @SuppressLint("SetTextI18n")
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
        val nowCheck:CheckBox = requireView().findViewById(num+1)

        if(nowCheck.isChecked){
            val total = binding.total
            totalCredit -= nowText.tag.toString().toInt() - ("0").toInt()
            total.text = resources.getString(R.string.total) + totalCredit.toString()
        }

        layout.removeView(nowText);
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
