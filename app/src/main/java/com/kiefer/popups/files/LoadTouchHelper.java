package com.kiefer.popups.files;

/** Identical to TracksTouchHelper except for the type of adapter provided.. should be an easy fix **/

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

public class LoadTouchHelper  extends ItemTouchHelper.SimpleCallback {

    private LoadAdapter adapter;

    private boolean dragging = false; //true while dragging to avoid updating until the drag is complete

    public LoadTouchHelper(LoadAdapter adapter){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    private int dragStart;

    //gets called every insert on a drag. After the first one dragStart gets setChecked, then dragging is
    //true until the drag is complete and clearView() gets called, where the dragStop gets setChecked.
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //Log.e("GGG", "TrackTouchHelper.onMove()");

        if(!dragging){
            dragStart = viewHolder.getAdapterPosition();
        }
        dragging = true;

        adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /*
    Called by the ItemTouchHelper when the user interaction with an element is over and it also
    completed its animation. Without a call to its super the chain won't update properly visually.
     */

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        int dragEnd = viewHolder.getAdapterPosition();

        //clearView is called after onMove so any drags or swipes are complete
        dragging = false;

        if(dragStart != dragEnd){
            adapter.moveFile(dragStart, dragEnd);
        }
        adapter.notifyDataSetChanged(); //this will update the listeners in the viewHolder to open the correct popups
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //
    }

    // DISABLE SWIPES
    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return 0;
    }
}