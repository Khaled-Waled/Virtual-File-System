import java.io.*;

public class CapManager {


    private static String filePath = "./capabilities.txt";

    CapManager()
    {
        File capabilitiesList = new File(filePath);
        try
        {
            capabilitiesList.createNewFile();
        }
        catch (IOException e)
        {
            System.out.println("Cannot create capabilities list !");
        }
    }

    public static boolean grantCapability(String userName, String path, String permissions)
    {
        BufferedReader reader = null;
        File capabilitiesFile = new File(filePath);
        try
        {
            int lineIndex = 0;
            reader = new BufferedReader(new FileReader(capabilitiesFile));
            String line;

            while((line = reader.readLine()) != null){

                String words[] = line.split(",");
                if(!words[0].equalsIgnoreCase(path)){
                    lineIndex++;
                    continue;
                }

                String currUserName;
                for(int i=1; i<words.length; i+=2){
                    currUserName = words[i];
                    if(userName.equals( currUserName)){
                        words[i+1] = permissions;
                        DiskManipulator.editLine(lineIndex,String.join(",", words), filePath);
                        return true;
                    }
                }

                DiskManipulator.editLine(lineIndex, String.join(",", String.join(",",words), userName, permissions), filePath);
                return true;

            }

            DiskManipulator.editLine(lineIndex, String.join( ",", path , userName, permissions), filePath);
            return true;



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }
}
