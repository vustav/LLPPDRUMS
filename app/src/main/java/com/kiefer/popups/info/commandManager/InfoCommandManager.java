package com.kiefer.popups.info.commandManager;

import android.util.Log;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class InfoCommandManager {
    private LinkedList<SelectInfoCommand> iUndoList; // Undo commands
    private LinkedList<SelectInfoCommand> iRedoList; // Redo commands
    private int iUndoLevel = 10; //hur många undos som sparas
    private final int FRONT = 0; //används för att ta bort första i undo-lista

    public InfoCommandManager() {
        iUndoList = new LinkedList<>();
        iRedoList = new LinkedList<>();
    }

    public boolean doCommand(SelectInfoCommand pCommand) {

        // Execute the command. Add to undo list if successful and is undoable
        if(pCommand.execute()) {
            if(pCommand.isUndoable()) {
                addUndo(pCommand);
                // A new undoable command clears RedoList
                clearRedoList();
            }
            return true;
        }
        else {
            return false;
        }
    }

    public void clearRedoList() {
        clearList(iRedoList);
    }

    public boolean undo() {
        if (canUndo()) {
            SelectInfoCommand pCommand = popUndo();
            if (pCommand.unExecute())
                addRedo(pCommand);
            return true;
        }
        return false;
    }

    public boolean redo() {
        if (canRedo()) {
            SelectInfoCommand pCommand = popRedo();
            if (pCommand.execute())
                addUndo(pCommand);
            return true;
        }
        return false;
    }

    public boolean canUndo() {
        return (iUndoList.size() > 0);
    }

    public boolean canRedo()   {
        return (iRedoList.size() > 0);
    }

    private void addUndo(SelectInfoCommand pCommand) {
        if (iUndoList.size() >= iUndoLevel) {
            iUndoList.remove(FRONT);
        }
        iUndoList.add(pCommand);
    }

    private void addRedo(SelectInfoCommand pCommand) {
        iRedoList.add(pCommand);
    }

    private void clearList(LinkedList<SelectInfoCommand> pList) {
        pList.clear();
    }

    private SelectInfoCommand popUndo() { // Pop the most recent command from list
        return iUndoList.removeLast();
    }

    private SelectInfoCommand popRedo() {  // Pop the most recent command from list
        return iRedoList.removeLast();
    }

    public SelectInfoCommand getLastUndo(){
        try {
            return iUndoList.getLast();
        }
        catch (NoSuchElementException exception){
            Log.e("InfoCommandManager", exception.getMessage());
            return null;
        }
    }

    public SelectInfoCommand getLastRedo(){
        try {
            return iRedoList.getLast();
        }
        catch (NoSuchElementException exception){
            Log.e("InfoCommandManager", exception.getMessage());
            return null;
        }
    }
}
