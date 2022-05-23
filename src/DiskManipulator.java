import javax.swing.*;
import java.io.*;
import java.util.Scanner;

import static java.lang.System.exit;

public class DiskManipulator
{
    private static File disk;
    private static int numOfBlocks;
    private static int pointerPos;
    public static Allocator allocator;
    public static int MAX_Entries;

    public DiskManipulator()
    {
        MAX_Entries = getRandomNumber(10,20);
        String newPath = "./structure.vfs";
        try
        {
            disk = new File(newPath);
            //if it already exists:
            //read data from file
            if(!disk.createNewFile())
            {
                String line = getLine(0);
                if(line != null)
                {
                    String [] arr = line.split(" ");
                    numOfBlocks = Integer.parseInt(arr[0]);
                    initAllocator(Integer.parseInt(arr[1]));
                }

                line = getLine(1);
                if(line != null)
                {
                    FreeSpaceManager.setBlocksState(line);
                }
            }
            else
            {
                //Disk parameters
                System.out.println("******Please enter disk details******");
                Scanner scanner = new Scanner(System.in);

                System.out.println("Please enter number of blocks:- ");
                numOfBlocks = scanner.nextInt();

                //Choice of allocation technique
                System.out.println("Enter allocation technique:-");
                System.out.println("1- Contiguous Allocation");
                System.out.println("2- Indexed Allocation");
                System.out.println("3- Linked Allocation");
                int option = scanner.nextInt();
                initAllocator(option);

                //Execute formatting with new parameters
                formatDisk(newPath, numOfBlocks, option);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    private static void initAllocator(int option)
    {
        if(option == 1)
        {
            allocator = new ContiguousAllocator();
        }
        else if (option == 2)
        {
            allocator = new IndexedAllocator();
        }
        else if (option == 3)
        {
            allocator = new LinkedAllocator();
        }
        else
        {
            exit(5000);
        }
    }


    public static int addEntry(String path, String entry)
    {

        return 0;
    }

    public static void editLine(int index, String newLine)
    {
        File fileToBeModified = new File(disk.getPath());
        String content = "";
        BufferedReader reader = null;
        FileWriter writer = null;
        try
        {
            reader = new BufferedReader(new FileReader(fileToBeModified));
            String line = reader.readLine();
            int currInd = 0;
            boolean edited = false;
            while (line != null)
            {
                if(currInd == index)
                {
                    line = newLine;
                    edited = true;
                }
                content = content + line + System.lineSeparator();
                line = reader.readLine();
                currInd++;
            }
            if(!edited)
            {
                while (currInd<index)
                {
                    content = content + System.lineSeparator();
                    currInd++;
                }
                content = content + newLine + System.lineSeparator();
            }
            writer = new FileWriter(fileToBeModified);
            writer.write(content);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                reader.close();
                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        if(newLine!="")
        {
            FreeSpaceManager.modifyBlock(index,'1');
        }
        else
        {
            FreeSpaceManager.modifyBlock(index,'0');
        }
    }

    public static String getLine(int index)
    {
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new FileReader(disk));
            String line = reader.readLine();
            int currInd = 0;
            while (line != null)
            {
                if(currInd == index)
                {
                    return line;
                }
                line = reader.readLine();
                currInd++;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
    //call this function to initialize the disk for the first time
    public static void formatDisk(String newPath, int numOfBlocks, int allocTechnique)
    {
        DiskManipulator.numOfBlocks = numOfBlocks;
        pointerPos = 0; //TODO change to start after metadata section
        FreeSpaceManager.resetBlockState();

        try
        {
            if(disk != null) disk.delete(); //delete old desk file if it exists
            disk = new File(newPath);       //create new file for the disk
            boolean newFileCreated = disk.createNewFile();
        }
        catch (IOException e)
        {
            System.out.println("Error: File structure not created");
            e.printStackTrace();
            exit(1);
        }

        //write metadata section
        String line = numOfBlocks+" "+allocTechnique;
        initAllocator(allocTechnique);
        editLine(0,line);
        editLine(1,FreeSpaceManager.getBlocksState());


        //write root directory
        String rootLine = "root 0";
        editLine(2, rootLine);

        for(int i=2+1; i< Math.min(DiskManipulator.getNumOfBlocks(), 2+ MAX_Entries); i++)
        {
            DiskManipulator.editLine(i,".");
        }
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


    public static int getNumOfBlocks()
    {
        return numOfBlocks;
    }
}
