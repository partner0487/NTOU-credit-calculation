package ntou.android2024.ntou_credit_calculation.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ntou.android2024.ntou_credit_calculation.R

class MarksRecyclerAdapter(private val subjectMarksList: List<subject>) :
    RecyclerView.Adapter<MarksRecyclerAdapter.MarksViewHolder>() {
    class MarksViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val subjectName: TextView = view.findViewById(R.id.txt_subject_recy)
        val subjectMarks: TextView = view.findViewById(R.id.txt_credit_recy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarksViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pdf_item_row, parent, false)
        return MarksViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarksViewHolder, position: Int) {
        holder.subjectName.text = subjectMarksList[position].subjectName
        holder.subjectMarks.text = subjectMarksList[position].credit.toString()
    }

    override fun getItemCount(): Int {
        return subjectMarksList.size
    }
}