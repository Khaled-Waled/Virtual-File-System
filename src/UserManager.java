import java.io.*;
import java.util.Scanner;

public class UserManager
{
    private static String currentUser = null;


    UserManager()
    {
        currentUser = null;

        //Create files
        File userList = new File("./user.txt");
        File capabilitiesList = new File("./capabilities.txt");
        try
        {
            userList.createNewFile();
            capabilitiesList.createNewFile();
        }
        catch (IOException e)
        {
            System.out.println("Cannot create users list !");
        }
    }

    public static boolean createUser(String userName, String password)
    {
        if(searchForUser(userName,password,true))
            return false;

        try
        {
            FileWriter fileWriter = new FileWriter("./user.txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(userName+" ,"+password);
            bufferedWriter.close();
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    public static boolean login(String userName, String password)
    {
        //Check if this user exists
        if(!searchForUser(userName,password,false))
            return false;


        //if he exists, set current user to him
        setCurrentUser(userName);
        return true;
    }


    private static boolean searchForUser(String userName, String password, boolean noPass)
    {
        BufferedReader reader = null;
        File usersFile = new File("./user.txt");
        try
        {
            reader = new BufferedReader(new FileReader(usersFile));
            String[] line = reader.readLine().split(" ,");
            if(line[0].equals(userName))
                if(noPass || line[1].equals(password))
                    return true;
        }
        catch (IOException e)
        {
            System.out.println("Error While reading file !");
            return false;
        }

        return false;
    }


    public static String getCurrentUser()
    {
        return currentUser;
    }
    private static void setCurrentUser(String newUser)
    {
        currentUser = newUser;
    }
}
