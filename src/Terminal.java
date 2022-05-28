import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Terminal
{
    static Scanner scanner = new Scanner(System.in);
    public static void chooseCommandAction()
    {
        //Display terminal status
        String userName = UserManager.getCurrentUser();
        if(userName != null)
            System.out.println(userName +" >> Accepting commands:");
        else
            System.out.println(">> Accepting commands:");

        //Parse command using CommandParser
        String line = scanner.nextLine();
        if (!CommandParser.parseCommand(line))
        {
            System.out.println("The command: \"" + CommandParser.getCommandName() + '\"' + " is not recognised");
            return;
        }
        String command = CommandParser.getCommandName();


        //if no one is logged in try again
        if(UserManager.getCurrentUser()== null && !command.equalsIgnoreCase("login"))
        {
            System.out.println("You must be logged in to make any action !");
            System.out.println("Try using the command:\nLogin user pass");
            return;
        }

        //Do command
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
            FreeSpaceManager.displayDiskStatus();
        }
        else if (Objects.equals(command, "DisplayDiskStructure"))
        {
            FreeSpaceManager.displayDiskStructure();
        }
        else if(command.equalsIgnoreCase("TellUser"))
        {
            System.out.println("You are: "+ UserManager.getCurrentUser());
        }
        else if(command.equalsIgnoreCase("CUser"))
        {
            List<String> args = CommandParser.getArgs();
            if(args.size()< 2)
            {
                System.out.println("Too few arguments");
                return;
            }
            if(!UserManager.login(args.get(0), args.get(1)))
                System.out.println("User Creation Failed");
            else
                System.out.println("Successfully created the user: "+ UserManager.getCurrentUser()+" !");
        }
        else if(command.equalsIgnoreCase("Grant"))
        {
            //TODO
        }
        else if(command.equalsIgnoreCase("Login"))
        {
            List<String> args = CommandParser.getArgs();
            if(args.size()< 2)
            {
                System.out.println("Too few arguments");
                return;
            }
            if(!UserManager.login(args.get(0), args.get(1)))
                System.out.println("Login Failed");
            else
                System.out.println("Hello "+ UserManager.getCurrentUser()+" !");
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
