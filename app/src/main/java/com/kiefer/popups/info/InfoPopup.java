package com.kiefer.popups.info;

import androidx.core.content.ContextCompat;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.info.Info;
import com.kiefer.info.InfoHolder;
import com.kiefer.popups.Popup;
import com.kiefer.popups.info.commandManager.InfoCommandManager;
import com.kiefer.popups.info.commandManager.SelectInfoCommand;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;

public class InfoPopup extends Popup {
    private final FrameLayout infoArea;
    private final ScrollView infoViewSV;
    private final ScrollView infoListSV;
    private final View popupView; //we need a reference to set scrollY when undoing

    private final InfoCommandManager infoCommandManager;
    private final ArrayList<InfoNode> infoNodes;
    private InfoNode selectedNode;


    private final int extraMargin = 10;

    public InfoPopup(final LLPPDRUMS llppdrums, String infoKey){ //skapa med new MainInfo() (ex) och ta layout från den som kommer in. Samma i menyn: skapa ny och ta dess layout
        super(llppdrums);

        //inflate the View
        popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_info_manager, null);
        popupView.setBackground(ContextCompat.getDrawable(llppdrums, ImgUtils.getRandomImageId())); //randomized image every time it opens

        //create the popupWindow
        int width = (int) llppdrums.getResources().getDimension(R.dimen.defaultSeekBarWidth) + (int) llppdrums.getResources().getDimension(R.dimen.autoViewHolderWidth) + (int) llppdrums.getResources().getDimension(R.dimen.fxViewHolderWidth) + (int) llppdrums.getResources().getDimension(R.dimen.marginLarge) * 5;
        int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        //setuo the commandManager
        infoCommandManager = new InfoCommandManager();

        //setup the nodes
        infoNodes = new ArrayList<>();
        setupNodes(llppdrums.getInfoManager()); //use infoManager here, inner infoHolders will automatically be dealt with

        //setup the list
        infoListSV = popupView.findViewById(R.id.infoListScroll);
        LinearLayout listLayout = popupView.findViewById(R.id.infoListLayout);
        for(InfoNode infoNode : infoNodes){
            listLayout.addView(infoNode.getTv());
        }

        //setup undo/redo
        ImageView undoBtn = popupView.findViewById(R.id.infoManagerUndoBtn);
        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //save pos before the undo
                float pos = infoViewSV.getScrollY();

                //perform the undo
                //infoCommandManager.undo();

                //now the last undo-command before the undo is the last redo, set pos as its posTo
                if(infoCommandManager.undo() && infoCommandManager.getLastRedo() != null) {
                    infoCommandManager.getLastRedo().setPosTo(pos); //update the scrollView in the infoFrame
                    setListSVPosition(infoCommandManager.getLastRedo().getNodeTo().getIndex()); //update the scrollView in the list
                }
            }
        });

        ImageView redoBtn = popupView.findViewById(R.id.infoManagerRedoBtn);
        redoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //same with pos but reversed
                float pos = infoViewSV.getScrollY();

                //infoCommandManager.redo();

                if(infoCommandManager.redo() && infoCommandManager.getLastUndo() != null){
                    infoCommandManager.getLastUndo().setPosFrom(pos); //update the scrollView in the infoFrame
                    setListSVPosition(infoCommandManager.getLastUndo().getNodeTo().getIndex()); //update the scrollView in the list
                }
            }
        });

        //setup the scrollView the infos are in
        infoViewSV = popupView.findViewById(R.id.infoManagerInfoSV);

        //setup the infoArea
        infoArea = popupView.findViewById(R.id.infoManagerInfoLayout);

        //set the selected Nod
        setSelectedNode(getNode(infoKey), 0);

        //set this to infoPopup in infoManager
        llppdrums.getInfoManager().setInfoPopup(this);

        //remove this as popupWindow on dismissal
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                llppdrums.getInfoManager().destroyInfoPopup();
            }
        });

        //show the popup with a little offset
        show(popupWindow);

        //adjust the list to show the selected node


        //this will run after the UI is ready
        llppdrums.getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                setListSVPosition(infoKey);
            }

        });

    }

    //this will automagically add a margin to Infos within inner InfoHolders
    private void setupNodes(InfoHolder infoHolder){
        setupNodes(infoHolder, 0);
    }

    private int nodeIndex = 0; //used the give the nodes an index in the order they're created
    private void setupNodes(InfoHolder infoHolder, int margin){
        for(Info info : infoHolder.getInfos()){
            InfoNode infoNode = new InfoNode(info, margin, nodeIndex++);
            infoNode.setDeselctedColor();
            infoNodes.add(infoNode);

            if(info instanceof InfoHolder){
                setupNodes(((InfoHolder)info), margin + extraMargin);
            }
        }
    }

    //returns the node with the corresponding key
    private InfoNode getNode(String key){
        for (InfoNode infoNode : infoNodes){
            if(infoNode.info.getKey().equals(key)){
                return infoNode;
            }
        }
        return null;
    }

    public void setSelectedNode(InfoNode infoNode, float y){
        selectedNode = infoNode;

        for(InfoNode node : infoNodes){
            node.setDeselctedColor();
        }

        infoNode.setSelectedColor();

        infoArea.removeAllViews();
        infoArea.addView(infoNode.getLayout());

        //only infoScrollView.setScrollY((int)y) doesn't work
        popupView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                infoViewSV.getViewTreeObserver().removeOnPreDrawListener(this);
                infoViewSV.setScrollY((int)y);
                return false;
            }
        });
    }

    public void setListSVPosition(String key){
        setListSVPosition(getNode(key).getIndex());
    }

    public void setListSVPosition(int index){
        float multiplier = (float) index / infoNodes.size();
        infoListSV.setScrollY((int)((float)infoListSV.getHeight() * multiplier));
    }

    public void fireSelectCommand(String key){
        float pos = infoViewSV.getScrollY();
        infoCommandManager.doCommand(new SelectInfoCommand(selectedNode, getNode(key), this, 0, pos));
    }

    //inner class that holds an info and it's layout in the list
    public class InfoNode{
        private final int index; //its index in the list to the left
        private final TextView tv;
        private final Info info;
        private final int deselectedBgColor;

        private InfoNode(Info info, int margin, int index){
            this.info = info;
            this.index = index;

            if(margin == 0){
                deselectedBgColor = ContextCompat.getColor(llppdrums, R.color.listDeselectedTier1);
            }
            else if(margin == extraMargin){
                deselectedBgColor = ContextCompat.getColor(llppdrums, R.color.listDeselectedTier2);
            }
            else if(margin == extraMargin * 2){
                deselectedBgColor = ContextCompat.getColor(llppdrums, R.color.listDeselectedTier3);
            }
            else if(margin == extraMargin * 3){
                deselectedBgColor = ContextCompat.getColor(llppdrums, R.color.listDeselectedTier4);
            }
            else{
                deselectedBgColor = ContextCompat.getColor(llppdrums, R.color.listDeselectedTier5);
            }

            tv = new TextView(llppdrums);
            tv.setText(info.getName());
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fireSelectCommand(info.getKey());
                }
            });

            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            llp.setMarginStart(margin);
            tv.setLayoutParams(llp);
        }

        public void setDeselctedColor(){
            tv.setBackgroundColor(deselectedBgColor);
            tv.setTextColor(ColorUtils.getContrastColor(deselectedBgColor));
        }

        public void setSelectedColor(){
            int color = ContextCompat.getColor(llppdrums, R.color.listSelected);
            tv.setBackgroundColor(color);
            tv.setTextColor(ColorUtils.getContrastColor(color));
        }

        private TextView getTv(){
            return tv;
        }

        //for now all Infos share label
        public ViewGroup getLayout(){
            return info.getLayout();
        }

        public int getIndex() {
            return index;
        }
    }

    /** GET **/
    public FrameLayout getInfoArea(){
        return infoArea;
    }
}
