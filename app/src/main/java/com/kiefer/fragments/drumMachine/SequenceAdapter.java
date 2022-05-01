package com.kiefer.fragments.drumMachine;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;

public class SequenceAdapter extends RecyclerView.Adapter<SequenceAdapter.SequenceTabViewHolder> {
    private final LLPPDRUMS llppdrums;

    public SequenceAdapter(LLPPDRUMS llppdrums) {
        this.llppdrums = llppdrums;
    }

    // Create new viewHolder
    @Override
    public SequenceAdapter.SequenceTabViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        FrameLayout trackView = (FrameLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tab_single_vertical, viewGroup, false);
        return new SequenceAdapter.SequenceTabViewHolder(trackView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final SequenceAdapter.SequenceTabViewHolder sequenceTabViewHolder, final int position) {
        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 200);
        sequenceTabViewHolder.view.setLayoutParams(flp);

        sequenceTabViewHolder.bg.setBackgroundColor(llppdrums.getDrumMachine().getSequences().get(sequenceTabViewHolder.getAdapterPosition()).getColor());
        sequenceTabViewHolder.tv.setText(llppdrums.getDrumMachine().getSequences().get(sequenceTabViewHolder.getAdapterPosition()).getName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return llppdrums.getDrumMachine().getNOfSequences();
    }

    /** VIEWHOLDER **/
    public static class SequenceTabViewHolder extends RecyclerView.ViewHolder {
        final FrameLayout view;

        final FrameLayout border;
        final FrameLayout bg;
        final TextView tv;

        public SequenceTabViewHolder(FrameLayout v) {
            super(v);
            view = v;
            border = v.findViewById(R.id.tabBorder);
            bg = v.findViewById(R.id.tabBg);
            tv = v.findViewById(R.id.tabTxt);
        }
    }
}