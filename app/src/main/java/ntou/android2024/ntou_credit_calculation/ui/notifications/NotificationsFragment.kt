package ntou.android2024.ntou_credit_calculation.ui.notifications

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
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
import androidx.recyclerview.widget.RecyclerView
import ntou.android2024.ntou_credit_calculation.R
import ntou.android2024.ntou_credit_calculation.databinding.FragmentNotificationsBinding
import java.io.File
import java.io.FileOutputStream


class NotificationsFragment : Fragment()  {

    private var _binding: FragmentNotificationsBinding? = null

    private val binding get() = _binding!!
    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){
        if(!it){
            Toast.makeText(requireContext(), "你必須打開權限", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val pdf: ImageView = binding.pdf
        pdf.setOnClickListener{
            val toast = Toast.makeText(context , "還沒匯出喔", Toast.LENGTH_SHORT)
            toast.show()
        }

        val outputPdf: Button = binding.outputPdf
        outputPdf.setOnClickListener{
            //這裡
            if(ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            else {
                val NameList = arguments?.getStringArrayList("subName")
                val CreditList = arguments?.getStringArrayList("credit")

                val subjectList = mutableListOf<subject>()

                if (NameList != null && CreditList != null) {
                    for (i in 0 until NameList.size) {
                        subjectList.add(subject(NameList.get(i), CreditList.get(i).toInt()))
                    }
                }
                val chunkedList = subjectList.chunked(12)

                val name = arguments?.getString("name")
                val studentId = arguments?.getString("id")

                var totalCredit = 0;
                val it = subjectList.iterator()
                while (it.hasNext()) {
                    val now = it.next()
                    totalCredit += now.credit
                }
                val pdfDocument = PdfDocument()
                var i = 0;
                while (i < chunkedList.size) {
                    val pdfDetails = PdfDetails(
                        name.toString(),
                        studentId.toString(),
                        totalCredit,
                        chunkedList.get(i)
                    )

                    val inflater = LayoutInflater.from(context)
                    val view = inflater.inflate(R.layout.pdf_layout, null)

                    val studentName = view.findViewById<TextView>(R.id.txt_student_name)
                    val studentID = view.findViewById<TextView>(R.id.txt_studentId)
                    val credit = view.findViewById<TextView>(R.id.txt_total_credit)
                    val recyclerView = view.findViewById<RecyclerView>(R.id.pdf_marks)

                    val adapter =
                        MarksRecyclerAdapter(pdfDetails.subjectList)     //Object for the recycler view adapter class

                    /*Assign values to each view using the data in PDFDetails*/
                    studentName.text = pdfDetails.Name
                    studentID.text = pdfDetails.studentId
                    credit.text = pdfDetails.totalCredit.toString()
                    recyclerView.adapter = adapter

                    val displayMetrics = DisplayMetrics()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        context?.display?.getRealMetrics(displayMetrics)
                        displayMetrics.densityDpi
                    } else {
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
                    val bitmap = Bitmap.createBitmap(
                        view.measuredWidth,
                        view.measuredHeight,
                        Bitmap.Config.ARGB_8888
                    )
                    val canvas = Canvas(bitmap)
                    view.draw(canvas)
                    Bitmap.createScaledBitmap(bitmap, 1050, 1485, true)

                    val pageInfo = PdfDocument.PageInfo.Builder(1050, 1485, i + 1).create()
                    val page = pdfDocument.startPage(pageInfo)
                    page.canvas.drawBitmap(bitmap, 0F, 0F, null)
                    pdfDocument.finishPage(page)

                    i += 1
                }
                val filePath = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "report.pdf"
                )
                pdfDocument.writeTo(FileOutputStream(filePath))
                pdfDocument.close()
                val toast = Toast.makeText(context, "匯出成功", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}