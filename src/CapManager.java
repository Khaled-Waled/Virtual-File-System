import java.io.*;
import java.util.Enumeration;

public class CapManager {


    private static String filePath = "./capabilities.txt";
    private static String defaultPermissions = "00";


    CapManager() {
        File capabilitiesList = new File(filePath);
        try {
            capabilitiesList.createNewFile();
        } catch (IOException e) {
            System.out.println("Cannot create capabilities list !");
        }
    }

    public static boolean grantCapability(String userName, String path, String permissions) {
        BufferedReader reader = null;
        File capabilitiesFile = new File(filePath);
        try {
            int lineIndex = 0;
            reader = new BufferedReader(new FileReader(capabilitiesFile));
            String line;

            while ((line = reader.readLine()) != null) {

                String words[] = line.split(",");
                if (!words[0].equalsIgnoreCase(path)) {
                    lineIndex++;
                    continue;
                }

                String currUserName;
                for (int i = 1; i < words.length; i += 2) {
                    currUserName = words[i];
                    if (userName.equals(currUserName)) {
                        words[i + 1] = permissions;
                        //path exists and user exists in permissions list
                        DiskManipulator.editLine(lineIndex, String.join(",", words), filePath);
                        return true;
                    }
                }

                //path exists but user doesn't exist in its permission list
                DiskManipulator.editLine(lineIndex, String.join(",", String.join(",", words), userName, permissions), filePath);
                return true;

            }

            //path doesn't exist
            DiskManipulator.editLine(lineIndex, String.join(",", path, userName, permissions), filePath);
            return true;


        }  catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }

    private static String getPermissions(String path, String userName) {

        BufferedReader reader = null;
        File capabilitiesFile = new File(filePath);

        try {
            reader = new BufferedReader(new FileReader(capabilitiesFile));
            String line;

            while ((line = reader.readLine()) != null) {

                String words[] = line.split(",");
                if (!words[0].equalsIgnoreCase(path)) {
                    continue;
                }

                String currUserName;
                for (int i = 1; i < words.length; i += 2) {
                    currUserName = words[i];
                    if (userName.equals(currUserName)) {
                        //path exists and user exists in permissions list
                        return words[i + 1];
                    }
                }

                //path exists but user doesn't exist in its permission list
                return defaultPermissions;
            }

            //path doesn't exist
            return defaultPermissions;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return defaultPermissions;
    }

    public static boolean hasPermission(String rawPath , boolean create)
    {
        if(UserManager.getCurrentUser().equals("admin"))
            return true;
        String[] path = rawPath.split("/");
        int len = path.length;
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<len-1; i++){
            sb.append(path[i]);
            if(i < len-2)
                sb.append("/");
        }
        String newPath = sb.toString();

        if(create){
            if(CapManager.getPermissions(newPath, UserManager.getCurrentUser()).charAt(0) != '1') {
                System.out.println("You don't have the appropriate permissions, sorry :(");
                return false;
            }
            return true;
        }
        else{
            if(CapManager.getPermissions(newPath, UserManager.getCurrentUser()).charAt(1) != '1') {
                System.out.println("You don't have the appropriate permissions, sorry :(");
                return false;
            }
            return true;
        }

    }

}
