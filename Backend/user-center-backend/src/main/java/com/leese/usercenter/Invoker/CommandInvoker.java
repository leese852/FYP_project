package com.leese.usercenter.Invoker;

import com.leese.usercenter.command.Command;

import java.util.ArrayList;
import java.util.List;
public class CommandInvoker { 
    private final List<Command> commandHistory = new ArrayList<>(); 
    public void executeCommand(Command command) { 
        command.execute(); 
        commandHistory.add(command); } 
        public List<Command> getHistory() { 
            return commandHistory; } 
        }