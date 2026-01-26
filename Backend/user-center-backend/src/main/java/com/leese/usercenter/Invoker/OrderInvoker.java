package com.leese.usercenter.Invoker;
import com.leese.usercenter.command.Command;
// OrderInvoker.java
public class OrderInvoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void invoke() {
        if (command != null) {
            command.execute();
        }
    }
}
