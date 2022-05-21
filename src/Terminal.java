import java.util.Objects;
import java.util.Scanner;

public class Terminal
{
    static Scanner scanner = new Scanner(System.in);
    public static void chooseCommandAction()
    {
        System.out.println(">> Accepting commands:");
        String line = scanner.nextLine();
        if (!CommandParser.parseCommand(line))
        {
            System.out.println("The command: \"" + CommandParser.getCommandName() + '\"' + " is not recognised");
            return;
        }

        String command = CommandParser.getCommandName();
        if(Objects.equals(command, "CreateFile"))
        {
            //TODO
            System.out.println("CREATING FILE...");
        }
        else if (Objects.equals(command, "CreateFolder"))
        {
            //TODO
            System.out.println("CREATING FOLDER...");
        }
        else if (Objects.equals(command, "DeleteFile"))
        {
            //TODO
            System.out.println("DELETING FILE...");
        }
        else if (Objects.equals(command, "DeleteFolder"))
        {
            //TODO
            System.out.println("DELETING FOLDER...");
        }
        else if (Objects.equals(command, "DisplayDiskStatus"))
        {
            //TODO
            System.out.println("DISK STATUS:        NO INITIALIZED");
        }
        else if (Objects.equals(command, "DisplayDiskStructure"))
        {
            //TODO
            System.out.println("DISK STRUCTURE:     EMPTY :O");
        }
        else if (command.equalsIgnoreCase("exit"))
        {
            //TODO
            System.exit(0);
        }
        else
        {
            System.out.println("ERROR !!!!");
        }

    }
}