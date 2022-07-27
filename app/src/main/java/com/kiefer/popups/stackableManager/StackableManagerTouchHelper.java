package com.kiefer.popups.stackableManager;

/** Identical to TracksTouchHelper except for the type of adapter provided.. should be an easy fix **/

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

public class StackableManagerTouchHelper extends ItemTouchHelper.SimpleCallback {

    private StackableManagerAdapter adapter;

    private boolean dragging = false; //true while dragging to avoid updating until the drag is complete

    // true when onMove() is called. Otherwise a swipe will be made with the old dragStart-value when holding an item and releasing before onMove() is called (which happens when items trade places).
    public boolean dragDone = false;

    private int dragStart;

    public StackableManagerTouchHelper(StackableManagerAdapter adapter){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    //gets called every insert on a drag. After the first one dragStart gets setChecked, then dragging is
    //true until the drag is complete and clearView() gets called, where the dragStop gets setChecked.
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //Log.e("GGG", "TrackTouchHelper.onMove()");

        if(!dragging){
            dragStart = viewHolder.getAdapterPosition();
            dragging = true;
        }
        dragDone = true;

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

        if(dragStart != dragEnd && dragDone){
            adapter.moveStackable(dragStart, dragEnd);
            dragDone = false;
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

    public boolean isDragging() {
        return dragging;
    }
}