package ntou.android2024.ntou_credit_calculation.ui.notifications

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ntou.android2024.ntou_credit_calculation.R
import ntou.android2024.ntou_credit_calculation.databinding.FragmentNotificationsBinding
import java.io.File
import java.io.FileOutputStream


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
            //textView.text = arguments?.getBoolean("test").toString() + arguments?.getString("test2")

            val subjectList = mutableListOf(
                subject("國文領域(一)", arguments?.getBoolean("ch1").toString(), 2),
                subject("國文領域(二)", arguments?.getBoolean("ch2").toString(), 2),
                subject("大一英文(上)", arguments?.getBoolean("en1").toString(), 2),
                subject("大一英文(下)", arguments?.getBoolean( "en2" ).toString(), 2),
                subject("進階英文", arguments?.getBoolean( "en3" ).toString(), 2),
                subject("海洋科學概論", arguments?.getBoolean( "oc" ).toString(), 2),
                subject("人工智慧概論", arguments?.getBoolean( "ai" ).toString(), 2),
                subject("服務學習 愛校服務(一)", arguments?.getBoolean( "cl1" ).toString(), 0),
                subject("服務學習 愛校服務(二)", arguments?.getBoolean( "cl2" ).toString(), 0),
                subject("普通物理", arguments?.getBoolean( "phy" ).toString(), 3),
                subject("計算機概論", arguments?.getBoolean( "pc" ).toString(), 3),
                subject("微積分(上)", arguments?.getBoolean( "cal1" ).toString(), 3),
                subject("微積分(下)", arguments?.getBoolean( "cal2" ).toString(), 3),
                subject("程式設計", arguments?.getBoolean( "prg" ).toString(), 3),
                subject("資料結構", arguments?.getBoolean( "ds" ).toString(), 3),
                subject("離散數學", arguments?.getBoolean( "dis" ).toString(), 3),
                subject("演算法", arguments?.getBoolean( "alr" ).toString(), 3),
                subject("數位邏輯", arguments?.getBoolean( "digl" ).toString(), 3),
                subject("數位邏輯實驗", arguments?.getBoolean( "digle" ).toString(), 3),
                subject("程式設計(二)", arguments?.getBoolean( "prg2" ).toString(), 3),
                subject("計算機組織學", arguments?.getBoolean( "ch" ).toString(), 3),
                subject("線性代數", arguments?.getBoolean( "la" ).toString(), 3),
                subject("機率論", arguments?.getBoolean( "prb" ).toString(), 3),
                subject("作業系統", arguments?.getBoolean( "os" ).toString(), 3),
                subject("電腦網路", arguments?.getBoolean( "net" ).toString(), 3),
                subject("資訊專題討論", arguments?.getBoolean( "prd" ).toString(), 3),
                subject("資工系專題(一)", arguments?.getBoolean( "pr1" ).toString(), 3),
                subject("資工系專題(二)", arguments?.getBoolean( "pr2" ).toString(), 3)
            )

            val name = arguments?.getString("name")
            val studentId = arguments?.getString("id")

            var totalCredit = 0;
            val it = subjectList.iterator()
            while(it.hasNext()) {
                val now = it.next()
                if (now.pass == "true")
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