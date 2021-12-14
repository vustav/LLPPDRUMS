package com.kiefer.popups.files;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.LLPPDRUMSKeeper;
import com.kiefer.utils.ColorUtils;

/**
 * Provide views to RecyclerView with data from  a dataSet.
 */
public class LoadAdapter extends RecyclerView.Adapter<LoadAdapter.LoadViewHolder>{
    private final LLPPDRUMS llppdrums;
    private final LoadPopup loadPopup;

    public LoadAdapter(LLPPDRUMS llppdrums, LoadPopup loadPopup) {
        this.llppdrums = llppdrums;
        this.loadPopup = loadPopup;
    }

    // Create new viewHolder
    @Override
    public LoadViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //inflate the layout and create a viewHolder
        View fxView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_load, viewGroup, false);
        return new LoadViewHolder(fxView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final LoadViewHolder loadViewHolder, final int position) {
        //Fx fx = loadPopup.getFxManager().getFxs().get(loadViewHolder.getAdapterPosition());
        final int pos = loadViewHolder.getAdapterPosition();

        //set the bgColor
        //loadViewHolder.bgView.setBackgroundColor(fx.getColor());

        //and the tv
        loadViewHolder.tv.setText(loadPopup.getContent().get(pos).getName());

        int color;
        if(pos % 2 == 0){
            color = ContextCompat.getColor(llppdrums, R.color.wavesBgEven);
        }
        else{
            color = ContextCompat.getColor(llppdrums, R.color.wavesBgUneven);
        }
        loadViewHolder.bgView.setBackgroundColor(color);
        loadViewHolder.tv.setTextSize((int) llppdrums.getResources().getDimension(R.dimen.defaultListTxtSize));
        loadViewHolder.tv.setTextColor(ColorUtils.getContrastColor(color));

        //set a listener for the viewHolder
        loadViewHolder.bgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loadPopup.getContent().get(pos).isFile()) {
                    llppdrums.load((LLPPDRUMSKeeper) llppdrums.getKeeperFileHandler().readKeepers(loadPopup.getContent().get(pos).getPath()));
                    llppdrums.getSequencer().notifyDataSetChange();
                }
                loadPopup.dismiss();
            }
        });

        //and the removeBtn (set it to invisible if it's not a file)
        if(loadPopup.getContent().get(pos).isFile()) {
            loadViewHolder.removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadPopup.removeFile(pos);
                }
            });
        }
        else{
            loadViewHolder.removeBtn.setVisibility(View.INVISIBLE);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return loadPopup.getContent().size();
    }

    public void moveFile(int from, int to){
        /** NOT USED **/
        //fxManagerPopup.moveFx(from, to);
    }

    /** VIEWHOLDER **/
    public static class LoadViewHolder extends RecyclerView.ViewHolder {
        final View bgView;
        final TextView tv;
        final Button removeBtn;

        public LoadViewHolder(View v) {
            super(v);
            bgView = v;
            tv = v.findViewById(R.id.loadViewHolderTV);
            removeBtn = v.findViewById(R.id.loadViewHolderRemoveBtn);
        }
    }
}