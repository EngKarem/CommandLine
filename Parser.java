package com.company;

public class Parser {

    String commandName;
    String[] args;
    public Parser() {
        commandName = null;
        args = null;
    }
    public boolean parse(String input)
    {

        if(!input.isEmpty())
        {
            args = input.split(" ");
            commandName = args[0];
            return true;
        }else
            return false;
    }


    public String getCommandName(){return commandName;}

    public String[] getArgs(){return args;}

}
