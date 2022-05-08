package com.kiefer.popups.fxManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.fx.Fx;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.ui.tabs.interfaces.Tab;

import java.util.ArrayList;

public class FxManagerAdapter extends RecyclerView.Adapter<FxManagerAdapter.FxViewHolder>{
    private final LLPPDRUMS llppdrums;
    private final FxManagerPopup fxManagerPopup;
    private final RecyclerView recyclerView;
    private final TabManager tabManager;

    public FxManagerAdapter(LLPPDRUMS llppdrums, FxManagerPopup fxManagerPopup, RecyclerView recyclerView) {
        this.llppdrums = llppdrums;
        this.fxManagerPopup = fxManagerPopup;
        this.recyclerView = recyclerView;
        tabManager = new TabManager(llppdrums);
    }

    // Create new viewHolder
    @Override
    public FxManagerAdapter.FxViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //inflate the layout and create a viewHolder
        FrameLayout fxView = (FrameLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tab_single_horizontal_btn, viewGroup, false);
        return new FxManagerAdapter.FxViewHolder(fxView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final FxManagerAdapter.FxViewHolder fxViewHolder, final int position) {
        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams((int) llppdrums.getResources().getDimension(R.dimen.fxViewHolderWidth), 100);
        fxViewHolder.view.setLayoutParams(flp);

        Fx fx = fxManagerPopup.getFxManager().getFxs().get(fxViewHolder.getAdapterPosition());

        //set the bgColor
        fxViewHolder.bg.setBackground(fx.getTabGradient());

        //and the tv
        fxViewHolder.tv.setText(fx.getName());

        //set a listener for the viewHolder
        fxViewHolder.bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fxManagerPopup.selectFx(fxViewHolder.getAdapterPosition());
                updateBorders(fxViewHolder.getAdapterPosition());
            }
        });

        //and the removeBtn
        fxViewHolder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fxManagerPopup.removeFx(fxViewHolder.getAdapterPosition());
            }
        });
    }

    public void updateBorders(int selectedTabIndex){

        ArrayList<FrameLayout> tabLayouts = new ArrayList<>();
        //RecyclerView rv = FxManagerAdapter2.getRecyclerView();
        for(int i = 0; i < recyclerView.getChildCount(); i++){
            tabLayouts.add(((FxManagerAdapter.FxViewHolder)recyclerView.getChildViewHolder(recyclerView.getChildAt(i))).view);
        }

        tabManager.setTabBorders(tabLayouts, selectedTabIndex, Tab.VERTICAL);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return fxManagerPopup.getFxManager().getFxs().size();
    }

    public void moveFx(int from, int to){
        fxManagerPopup.moveFx(from, to);
    }

    /** VIEWHOLDER **/
    //public static class FxViewHolder extends RecyclerView.ViewHolder implements Tab {
    public static class FxViewHolder extends RecyclerView.ViewHolder {
        final FrameLayout view;

        final FrameLayout border;
        final RelativeLayout bg;
        final TextView tv;
        final Button removeBtn;

        public FxViewHolder(FrameLayout v) {
            super(v);
            view = v;
            border = v.findViewById(R.id.tabBorder);
            bg = v.findViewById(R.id.tabBg);
            tv = v.findViewById(R.id.tabTxt);
            removeBtn = v.findViewById(R.id.tabBtn);
        }
    }
}