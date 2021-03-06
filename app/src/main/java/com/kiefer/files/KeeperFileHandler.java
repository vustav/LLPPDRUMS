package com.kiefer.files;

import android.util.Log;
import android.widget.Toast;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.Keeper;
import com.kiefer.popups.files.SavePopup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class KeeperFileHandler {
    private LLPPDRUMS llppdrums;

    public KeeperFileHandler(LLPPDRUMS context){
        this.llppdrums = context;
    }

    public Keeper readKeeper(String path){
        String errorMessage = "couldn't load file: ";
        return readKeeper(path, errorMessage);
    }

    public Keeper readKeeper(String path, String errorMessage){
        return (Keeper)readFile(path, errorMessage);
    }

    public Keeper readKeepers(String path){
        String errorMessage = "couldn't load file: ";
        return readKeepers(path, errorMessage);
    }

    public Keeper readKeepers(String path, String errorMessage){
        try {
            return (Keeper)readFile(path, errorMessage);
        }
        catch (Exception e){
            Log.e("KIEFER", e.getMessage());
            return null;
        }
    }

    public Object readFile(String path, String errorMessage){
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File(path)));
            return is.readObject();
        }
        catch (FileNotFoundException fnfe){
            Log.e("KIEFER", fnfe.getMessage());

            String message = errorMessage + fnfe.getMessage();
            Toast toast = Toast.makeText(llppdrums,
                    message, Toast.LENGTH_SHORT);
            toast.show();
        }
        catch (IOException ioe){
            Log.e("KIEFER", ioe.getMessage());

            String message = errorMessage + ioe.getMessage();
            Toast toast = Toast.makeText(llppdrums,
                    message, Toast.LENGTH_SHORT);
            toast.show();
        }
        catch (ClassNotFoundException cnfe){
            Log.e("KIEFER", cnfe.getMessage());

            String message = errorMessage + cnfe.getMessage();
            Toast toast = Toast.makeText(llppdrums,
                    message, Toast.LENGTH_SHORT);
            toast.show();
        }
        return null;
    }

    public void writePromptName(Object keeper, String folder){
        new SavePopup(llppdrums, this, folder, keeper);
    }

    public void write(Object keeper, String folder, String name, boolean toastMsg){
        String errorMessage = "couldn't save file: ";
        try {
            String path = folder + "/" + name + llppdrums.getString(R.string.templateExtension);

            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(path));
            os.writeObject(keeper);
            os.close();

            if(toastMsg) {
                String message = path.substring(folder.length() + 1) + " saved.";
                Toast toast = Toast.makeText(llppdrums,
                        message, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        catch (IOException ioe){
            Log.e("KIEFER", ioe.toString() + " KeeperFileHandler.write()");

            String message = errorMessage + ioe.getMessage();
            Toast toast = Toast.makeText(llppdrums,
                    message, Toast.LENGTH_SHORT);
            toast.show();

        }
        catch (Exception e){
            Log.e("KIEFER", e.toString() + "  KeeperFileHandler.write()");

            String message = errorMessage + e.getMessage();
            Toast toast = Toast.makeText(llppdrums,
                    message, Toast.LENGTH_SHORT);
            toast.show();

        }
    }

    public void deleteFile(String path){
        //delete the file
        File file = new File(path);
        file.delete();
    }
}
