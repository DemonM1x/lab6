package org.example.Command;

import org.example.Collection.City;
import org.example.CommandManager;
import org.example.interfaces.Command;
import org.example.interfaces.CommandManagerCustom;

public class ShowCommand extends CommandBase implements Command {
    public ShowCommand(CommandManagerCustom commandManager){
        super(commandManager);
    }

    @Override
    public boolean execute(String[] args) {
        var commandMessageHandler = commandManager.getMessageHandler();
        var cities = commandManager.getCollectionManager().get();
        if(cities == null || cities.size() == 0){
            commandMessageHandler.displayToUser("there is no cities yet.. add a new one");
            return true;
        }
        for (City city : cities) {
            String mess = city.toString();
            commandMessageHandler.displayToUser(mess); //TODO:
        }
        return true;
    }

    @Override
    public String getInfo() {
        return "printing collection elements into the string representation";
    }
}