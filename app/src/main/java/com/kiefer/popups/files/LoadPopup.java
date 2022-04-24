package com.kiefer.popups.files;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.popups.Popup;

import java.io.File;
import java.util.ArrayList;

public class LoadPopup extends Popup {
    private final PopupWindow popupWindow;
    private final RecyclerView loadRecyclerView;
    private final LoadAdapter loadAdapter;

    private ArrayList<KiffFile> content = new ArrayList<>();

    public LoadPopup(LLPPDRUMS llppdrums){
        super(llppdrums);

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_load, null);
        //popupView.setBackground(ContextCompat.getDrawable(llppdrums, oscillatorManager.getWavePopupImageId(oscNo)));
        popupView.findViewById(R.id.loadBgIV).setBackground(ContextCompat.getDrawable(llppdrums, llppdrums.getDrumMachine().getLoadPopupBgId()));

        //create the popupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        setupContent();

        //setup the fx-recyclerView
        loadRecyclerView = popupView.findViewById(R.id.loadRecyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        LinearLayoutManager fxLayoutManager = new LinearLayoutManager(llppdrums);
        fxLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        loadRecyclerView.setLayoutManager(fxLayoutManager);

        //create an adapter
        loadAdapter = new LoadAdapter(llppdrums, this);

        //add the adapter to the recyclerView
        loadRecyclerView.setAdapter(loadAdapter);

        show(popupWindow);
    }

    private void setupContent(){

        //list all files in the directory
        File directory = new File(llppdrums.getSavedProjectsFolderPath());
        File[] content = directory.listFiles();

        //create the ArrList of strings with filenames
        if(content != null) {

            //only include files ending with templateExtension
            this.content = new ArrayList<>();
            if(content.length != 0) {
                for (File file : content) {
                    KiffFile kf = new KiffFile(file.toString(), file.toString().substring(llppdrums.getSavedProjectsFolderPath().length() + 1), true);
                    if (kf.getPath().substring(kf.getPath().length() - llppdrums.getString(R.string.templateExtension).length()).equals(llppdrums.getString(R.string.templateExtension))) {
                        this.content.add(kf);
                    }
                }
            }
            //if no files exist, create a KiffFile with isFile = false, just to get a string to shoe in the window
            else{
                KiffFile kf = new KiffFile(null, llppdrums.getString(R.string.noSavedFilesMessage), false);
                this.content.add(kf);
            }
        }
    }

    protected void removeFile(int pos){
        llppdrums.getKeeperFileHandler().deleteFile(content.get(pos).getPath());
        content.remove(pos);
        loadAdapter.notifyDataSetChanged();
    }

    protected ArrayList<KiffFile> getContent(){
        return content;
    }

    void dismiss(){
        popupWindow.dismiss();
    }

    /** INNER CLASS **/

    //isFile is used when there are no files saved. In that case we create a KiffFile with the name "NO FILES SAVED". That way we can use the same Adapter.
    public static class KiffFile{
        String path;
        String name;
        boolean isFile;

        public KiffFile(String path, String name, boolean isFile){
            this.path = path;
            this.name = name;
            this.isFile = isFile;
        }

        public String getPath() {
            return path;
        }

        public String getName() {
            return name;
        }

        public boolean isFile() {
            return isFile;
        }
    }
}
