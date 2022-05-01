package com.kiefer.popups.fxManager;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kiefer.R;
import com.kiefer.machine.fx.Fx;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.ui.tabs.interfaces.Tab;

import java.util.ArrayList;

/**
 * Provide views to RecyclerView with data from  a dataSet.
 */
public class FxManagerAdapter extends RecyclerView.Adapter<FxManagerAdapter.FxViewHolder>{
    private final FxManagerPopup fxManagerPopup;
    private final RecyclerView recyclerView;

    public FxManagerAdapter(FxManagerPopup fxManagerPopup, RecyclerView recyclerView) {
        this.fxManagerPopup = fxManagerPopup;
        this.recyclerView = recyclerView;
    }

    // Create new viewHolder
    @Override
    public FxViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //inflate the layout and create a viewHolder
        View fxView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_popup_fx_manager_fx, viewGroup, false);
        return new FxViewHolder(fxView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final FxViewHolder fxViewHolder, final int position) {
        Fx fx = fxManagerPopup.getFxManager().getFxs().get(fxViewHolder.getAdapterPosition());

        //set the bgColor
        //fxViewHolder.bgView.setBackgroundColor(fx.getBgGradient());
        fxViewHolder.bgView.setBackgroundDrawable(fx.getTabGradient());

        //and the tv
        fxViewHolder.tv.setText(fx.getName());

        //set a listener for the viewHolder
        fxViewHolder.bgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fxManagerPopup.selectFx(fxViewHolder.getAdapterPosition());

                /*
                ArrayList<Tab> viewHolders = new ArrayList<>();
                for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; ++i) {
                    final FxViewHolder holder = (FxViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                    viewHolders.add(holder);
                }
                fxManagerPopup.getFxManager().getTabManager().setTabBorders(0, viewHolders, fxViewHolder.getAdapterPosition());

                 */
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
        final View bgView;
        final TextView tv;
        final Button removeBtn;

        public FxViewHolder(View v) {
            super(v);
            bgView = v;
            tv = v.findViewById(R.id.fxViewHolderTV);
            removeBtn = v.findViewById(R.id.fxViewHolderRemoveBtn);
        }

        /*
        public int getBitmapId(){
            return 0;
        }
        public String getName(){
            return "";
        }
        public int getOrientation(){
            return Tab.VERTICAL;
        }
        public int getTier(){
            return 0;
        }
        public int getTabIndex(){
            return 0;
        }

         */
    }
}