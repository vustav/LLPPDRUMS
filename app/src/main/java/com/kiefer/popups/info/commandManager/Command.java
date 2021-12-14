package com.kiefer.popups.info.commandManager;

public interface Command {
    boolean execute();   // Execute the command
    boolean unExecute(); // Undo the command
    boolean isUndoable(); // Possible to unexecute?
}
