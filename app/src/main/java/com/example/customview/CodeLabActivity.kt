package com.example.customview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.customview.view.step1.Step1Activity
import kotlinx.android.synthetic.main.activity_code_lab.*


private val data = listOf(
    Step(
        "[ProgressBar]",
        "원형 프로그레스 바.",
        "진행상황에 따른 프로그레스 UI 처리.",
        Step1Activity::class
    )
)


class CodeLabActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_lab)
        rv_code_lab.run { adapter = MainAdapter(data) }
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, CodeLabActivity::class.java))
        }
    }
}

class MainAdapter(private val data: List<Step>) : RecyclerView.Adapter<MainViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return MainViewHolder(view as CardView)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(data[position])
    }

}

class MainViewHolder(private val cardView: CardView) : RecyclerView.ViewHolder(cardView) {
    private val header: TextView = cardView.findViewById(R.id.header)
    private val description: TextView = cardView.findViewById(R.id.description)
    private val caption: TextView = cardView.findViewById(R.id.caption)

    fun bind(step: Step) {
        header.text = step.number
        description.text = step.name
        caption.text = step.caption

        val context = cardView.context

        cardView.setOnClickListener {
            val intent = Intent(context, step.activity.java)
            context.startActivity(intent)
        }

        val color = if (step.highlight) {
            context.resources.getColor(R.color.secondaryLightColor)
        } else {
            context.resources.getColor(R.color.primaryTextColor)
        }

        header.setTextColor(color)
        description.setTextColor(color)
    }

}