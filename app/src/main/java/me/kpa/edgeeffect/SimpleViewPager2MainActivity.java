package me.kpa.edgeeffect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import me.kpa.edgeeffect.recycler.RecyclerViewVerticaEdgeEffect;


public class SimpleViewPager2MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;

    private ArrayList<String> mList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_simple_view_pager2_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewPager2 = ((ViewPager2) findViewById(R.id.viewPager));
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        for (int i = 0; i < 30; i++) {
            mList.add(i + " TEST - " + i);
        }

        viewPager2.setAdapter(new SimpleAdapter(mList));

        RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
        recyclerView.setEdgeEffectFactory(new RecyclerViewVerticaEdgeEffect());

    }


    static class SimpleAdapter extends RecyclerView.Adapter<RecyclerViewVerticaEdgeEffect.SpringVerticaAnimationViewHolder> {

        private ArrayList<String> mList = new ArrayList<>();

        public SimpleAdapter(ArrayList<String> mList) {
            this.mList = mList;
        }

        @NonNull
        @Override
        public RecyclerViewVerticaEdgeEffect.SpringVerticaAnimationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecyclerViewVerticaEdgeEffect.SpringVerticaAnimationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vp_simple_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewVerticaEdgeEffect.SpringVerticaAnimationViewHolder holder, int position) {
            ((TextView) holder.itemView.findViewById(R.id.textView)).setText("12 " + mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

}