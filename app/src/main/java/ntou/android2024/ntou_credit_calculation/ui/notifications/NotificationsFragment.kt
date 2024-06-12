package ntou.android2024.ntou_credit_calculation.ui.notifications

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ntou.android2024.ntou_credit_calculation.R
import ntou.android2024.ntou_credit_calculation.databinding.FragmentNotificationsBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class NotificationsFragment : Fragment()  {

    private var _binding: FragmentNotificationsBinding? = null

    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val loading = binding.loading

        val pdf: ImageView = binding.pdf
        pdf.visibility = View.GONE
        pdf.setOnClickListener{
            openFolder()
        }

        val outputPdf: Button = binding.outputPdf
        outputPdf.setOnClickListener{
            //這裡
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
            val nameList = ArrayList<String>()
            val creditList = ArrayList<String>()

            for (i in 2..< txt.size) {
                val dataArray = txt[i].replace("\"", "").split(",=")
                val credit = dataArray[3]
                val className = dataArray[5]

                nameList.add(className)
                creditList.add(credit)

            }

            val subjectList = mutableListOf<subject>()

            if (nameList != null && creditList != null) {
                for(i in 0 until nameList.size){
                    subjectList.add(subject(nameList[i], creditList[i].toInt()))
                }
            }
            val chunkedList = subjectList.chunked(12)

            val name = txt[0].split("姓名-")[1]
            val studentId = txt[0].split("姓名-")[0].split("學號-")[1]

            var totalCredit = 0;
            val iterator = subjectList.iterator()
            while(iterator.hasNext()) {
                val now = iterator.next()
                totalCredit += now.credit
            }
            val pdfDocument = PdfDocument()
            var i = 0;
            while(i < chunkedList.size){
                val pdfDetails = PdfDetails(name.toString(), studentId.toString(), totalCredit, chunkedList.get(i))

                val inf = LayoutInflater.from(context)
                val view = inf.inflate(R.layout.pdf_layout, null)

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
                Bitmap.createScaledBitmap(bitmap, 1050, 1485, true)

                val pageInfo = PdfDocument.PageInfo.Builder(1050, 1485, i + 1).create()
                val page = pdfDocument.startPage(pageInfo)
                page.canvas.drawBitmap(bitmap, 0F, 0F, null)
                pdfDocument.finishPage(page)

                i += 1
            }
            val filePath = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "report.pdf")
            pdfDocument.writeTo(FileOutputStream(filePath))
            pdfDocument.close()
            pdf.visibility = View.VISIBLE
            loading.visibility = View.GONE
            val gotoPdf = binding.gotoPdf
            gotoPdf.text = "點擊開啟PDF"
            val toast = Toast.makeText(context , "下載完成", Toast.LENGTH_SHORT)
            toast.show()
        }
        return root
    }

    @SuppressLint("SetTextI18n")
    private fun openFolder() {
        val intent = Intent(Intent.ACTION_VIEW)
        val selectedUri = Uri.parse(Environment.getExternalStorageDirectory().toString())
        intent.setDataAndType(selectedUri,  DocumentsContract.Document.MIME_TYPE_DIR)
        startActivity(intent);
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}