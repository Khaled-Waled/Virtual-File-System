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
        else if(command.equalsIgnoreCase("CUser")) {
            if (!UserManager.getCurrentUser().equalsIgnoreCase("admin"))
            {
                System.out.println("Sorry, you are not authorized to create a user.");
                return;
            }
            List<String> args = CommandParser.getArgs();
            if(args.size()< 2)
            {
                System.out.println("Too few arguments");
                return;
            }
            if(!UserManager.createUser(args.get(0), args.get(1)))
                System.out.println("User Creation Failed");
            else
                System.out.println("Successfully created the user: "+ args.get(0) +" !");
        }
        else if(command.equalsIgnoreCase("Grant"))
        {
            //TODO
            List<String> args = CommandParser.getArgs();
            if(args.size() != 3)
            {
                System.out.println("Too few arguments");
                return;
            }
            if (!UserManager.getCurrentUser().equalsIgnoreCase("admin"))
            {
                System.out.println("Sorry, you are not authorized to grant permissions.");
                return;
            }
            if(!UserManager.searchForUser(args.get(0), "",true))
            {
                System.out.println("User not found");
                return;
            }

            //Assuming path already exists
            boolean granted = CapManager.grantCapability(args.get(0), args.get(1), args.get(2));
            if(granted)
                System.out.println("Successfully granted capability");
            else
                System.out.println("Couldn't grant capability");

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
