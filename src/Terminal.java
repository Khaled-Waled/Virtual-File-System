import java.util.List;
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
            System.out.println("CREATING FILE...");
            List<String> args = CommandParser.getArgs();
            DiskManipulator.allocator.createFile(args.get(0), Integer.parseInt(args.get(1)));
        }
        else if (Objects.equals(command, "CreateFolder"))
        {
            System.out.println("CREATING FOLDER...");
            List<String> args = CommandParser.getArgs();
            DiskManipulator.allocator.createFolder(args.get(0));
        }
        else if (Objects.equals(command, "DeleteFile"))
        {
            System.out.println("DELETING FILE...");
            List<String> args = CommandParser.getArgs();
            DiskManipulator.allocator.deleteFile(args.get(0));
        }
        else if (Objects.equals(command, "DeleteFolder"))
        {
            System.out.println("DELETING FOLDER...");
            List<String> args = CommandParser.getArgs();
            if(!DiskManipulator.allocator.deleteFolder(args.get(0)))
                System.out.println("Cannot delete folder at "+args.get(0));;
        }
        else if (Objects.equals(command, "DisplayDiskStatus"))
        {
            FreeSpaceManager.displayDiskStructure();
        }
        else if (Objects.equals(command, "DisplayDiskStructure"))
        {
            FreeSpaceManager.displayDiskStructure();
        }
        else if (command.equalsIgnoreCase("exit"))
        {
            System.exit(0);
        }
        else
        {
            System.out.println("ERROR !!!!");
        }

    }
}
