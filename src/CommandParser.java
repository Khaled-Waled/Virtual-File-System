import java.util.Arrays;
import java.util.List;

public class CommandParser
{
    private static String function;
    private static List<String> arguments;
    private static final String[] possibleCommands = {
                    "CreateFile","CreateFolder",
                    "DeleteFile","DeleteFolder",
                    "DisplayDiskStatus","DisplayDiskStructure",
                    "exit"};
    public static boolean parseCommand(String command)
    {
        //get the position of first space
        int s= -1;
        for (int i=0; i<command.length();i++)
        {
            if (command.charAt(i)==' ')
            {
                s=i;
                break;
            }
        }

        if (s>0) //if there is a space (more than a word)
        {
            function = command.substring(0,s);
            command = command.substring(s+1);
            arguments = Arrays.asList(command.split(" "));
        }
        else // no arguments command
            function = command;




        //check if that command exists
        for (String element : possibleCommands) {
            if (function.equals(element)) {
                return true;
            }
        }
        return false;
    }

    public static String getCommandName()
    {
        return function;
    }
    public List<String> getArgs()
    {
        return arguments;
    }

}
