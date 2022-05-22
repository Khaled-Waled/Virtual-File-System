import java.io.*;
import java.util.Scanner;

import static java.lang.System.exit;

public class DiskManipulator
{
    private static File disk;
    private static int numOfBlocks;
    private static int pointerPos;
    public static Allocator allocator;

    public DiskManipulator(String newPath)
    {
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
                    numOfBlocks = Integer.parseInt(line);
                }
                System.out.println(numOfBlocks);

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

                System.out.println("Enter disk Path:-");
                newPath = scanner.next();
                //scanner.nextLine();

                System.out.println("Creating file at: "+ newPath);
                //Choice of allocation technique
                System.out.println("Enter allocation technique:-");
                System.out.println("1- Contiguous Allocation");
                System.out.println("2- Indexed Allocation");
                System.out.println("3- Linked Allocation");
                int option = scanner.nextInt();
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
                    exit(3);
                }

                //Execute formatting with new parameters
                formatDisk(newPath, numOfBlocks);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
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
    public static void formatDisk(String newPath, int numOfBlocks)
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
        editLine(0,String.valueOf(numOfBlocks));
        System.out.println("Blocks state = "+FreeSpaceManager.getBlocksState() );
        editLine(1,FreeSpaceManager.getBlocksState());


        //write root directory
        allocator.createFolder("root", 2);
    }



    public static int getNumOfBlocks()
    {
        return numOfBlocks;
    }
}
