package me.kpa.edgeeffect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.kpa.edgeeffect.recycler.RecyclerViewHorizontalEdgeEffect
import me.kpa.edgeeffect.recycler.RecyclerViewVerticaEdgeEffect

class SimpleRecyclerActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var recyclerView1: RecyclerView? = null
    private val mList = ArrayList<String>()

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_simple_recycler)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View>(R.id.main),
            OnApplyWindowInsetsListener { v: View, insets: WindowInsetsCompat ->
                val systemBars: Insets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })
        recyclerView = findViewById<View>(R.id.recycler) as RecyclerView?
        recyclerView1 = findViewById<View>(R.id.rv1) as RecyclerView?

        val adapter = SimpleAdapter(mList)
        recyclerView?.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        //GridLayoutManager
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        recyclerView?.setAdapter(adapter)
        recyclerView?.setEdgeEffectFactory(RecyclerViewVerticaEdgeEffect())

        recyclerView1?.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        val adapter1 = SimpleAdapter1(mList)
        recyclerView1?.setAdapter(adapter1)
        recyclerView1?.setEdgeEffectFactory(RecyclerViewHorizontalEdgeEffect())

        for (i in 0..29) {
            mList.add("$i TEST - $i")
        }
    }

    internal class SimpleAdapter(mList: ArrayList<String>) :
        RecyclerView.Adapter<RecyclerViewVerticaEdgeEffect.SpringVerticaAnimationViewHolder?>() {
        private var mList = ArrayList<String>()

        init {
            this.mList = mList
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerViewVerticaEdgeEffect.SpringVerticaAnimationViewHolder {
            return RecyclerViewVerticaEdgeEffect.SpringVerticaAnimationViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.simple_item, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        override fun onBindViewHolder(holder: RecyclerViewVerticaEdgeEffect.SpringVerticaAnimationViewHolder, position: Int) {
            (holder.itemView.findViewById(R.id.textView) as TextView).text =
                "12 " + mList[position]
        }

    }

    internal class SimpleAdapter1(mList: ArrayList<String>) :
        RecyclerView.Adapter<RecyclerViewHorizontalEdgeEffect.SpringHorizontalAnimationViewHolder?>() {
        private var mList = ArrayList<String>()

        init {
            this.mList = mList
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerViewHorizontalEdgeEffect.SpringHorizontalAnimationViewHolder {
            return RecyclerViewHorizontalEdgeEffect.SpringHorizontalAnimationViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.horizontal_simple_item, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        override fun onBindViewHolder(holder: RecyclerViewHorizontalEdgeEffect.SpringHorizontalAnimationViewHolder, position: Int) {
            (holder.itemView.findViewById(R.id.textView) as TextView).text = "12 " + mList[position]
        }

    }
}