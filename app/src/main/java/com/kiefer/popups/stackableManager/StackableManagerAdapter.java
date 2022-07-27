package com.kiefer.popups.stackableManager;

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
import com.kiefer.machine.sequence.track.Stackables.Stackable;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.ui.tabs.interfaces.Tab;

import java.util.ArrayList;

public class StackableManagerAdapter extends RecyclerView.Adapter<StackableManagerAdapter.StackableViewHolder>{
    private final LLPPDRUMS llppdrums;
    private final StackableManagerPopup stackableManagerPopup;
    private final RecyclerView recyclerView;
    private final TabManager tabManager;

    public StackableManagerAdapter(LLPPDRUMS llppdrums, StackableManagerPopup stackableManagerPopup, RecyclerView recyclerView) {
        this.llppdrums = llppdrums;
        this.stackableManagerPopup = stackableManagerPopup;
        this.recyclerView = recyclerView;
        tabManager = new TabManager(llppdrums);
    }

    // Create new viewHolder
    @Override
    public StackableViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //inflate the layout and create a viewHolder
        FrameLayout fxView = (FrameLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tab_single_horizontal_btn, viewGroup, false);
        return new StackableViewHolder(fxView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final StackableViewHolder stackableViewHolder, final int position) {
        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams((int) llppdrums.getResources().getDimension(R.dimen.fxViewHolderWidth), 100);
        stackableViewHolder.view.setLayoutParams(flp);

        Stackable stackable = stackableManagerPopup.getStackableManager().getStackables().get(stackableViewHolder.getAdapterPosition());

        //set the bgColor
        stackableViewHolder.bg.setBackground(stackable.getTabGradient());

        //and the tv
        stackableViewHolder.tv.setText(stackable.getName());

        //set a listener for the viewHolder
        stackableViewHolder.bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stackableManagerPopup.selectStackable(stackableViewHolder.getAdapterPosition());
                updateBorders(stackableViewHolder.getAdapterPosition());
            }
        });

        //and the removeBtn
        stackableViewHolder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stackableManagerPopup.removeStackable(stackableViewHolder.getAdapterPosition());
            }
        });
    }

    public void updateBorders(int selectedTabIndex){

        ArrayList<FrameLayout> tabLayouts = new ArrayList<>();
        //RecyclerView rv = FxManagerAdapter2.getRecyclerView();
        for(int i = 0; i < recyclerView.getChildCount(); i++){
            tabLayouts.add(((StackableViewHolder)recyclerView.getChildViewHolder(recyclerView.getChildAt(i))).view);
        }

        tabManager.setTabBorders(tabLayouts, selectedTabIndex, Tab.VERTICAL);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return stackableManagerPopup.getStackableManager().getStackables().size();
    }

    public void moveStackable(int from, int to){
        stackableManagerPopup.moveStackable(from, to);
    }

    /** VIEWHOLDER **/
    //public static class FxViewHolder extends RecyclerView.ViewHolder implements Tab {
    public static class StackableViewHolder extends RecyclerView.ViewHolder {
        final FrameLayout view;

        final FrameLayout border;
        final RelativeLayout bg;
        final TextView tv;
        final Button removeBtn;

        public StackableViewHolder(FrameLayout v) {
            super(v);
            view = v;
            border = v.findViewById(R.id.tabBorder);
            bg = v.findViewById(R.id.tabBg);
            tv = v.findViewById(R.id.tabTxt);
            removeBtn = v.findViewById(R.id.tabBtn);
        }
    }
}