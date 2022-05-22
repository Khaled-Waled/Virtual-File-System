import java.util.zip.Inflater;

public abstract class Allocator
{
    public abstract boolean createFile(String path, int size);
    public boolean createFolder(String path)
    {
        /*
        String line = dirName+" 0";
        DiskManipulator.editLine(block,line);
         */
        boolean state = true;
        String[] arr = path.split("/");
        int arrLen = arr.length;
        int iter = 1;
        String curr = arr[iter], parent = arr[iter-1];
        int STARTPOS = 2;
        int pointer = STARTPOS;

        boolean inProg = true;
        while(iter < arrLen)
        {
            if(iter == arrLen-1)
            {
                System.out.println("pointer = "+pointer);
                System.out.println(DiskManipulator.getLine(pointer));
                String[] head = DiskManipulator.getLine(pointer).split(" ");
                int len = Integer.parseInt(head[1]);
                for (int i = 1; i <= len; i++)
                {
                    String[] line = DiskManipulator.getLine(pointer + i).split(" ");
                    if (line[0].equalsIgnoreCase("0") && line[1].equalsIgnoreCase(curr))
                    {
                        //already created
                        return false;
                    }
                }

                //create folder entry
                String newLine = head[0]+" "+String.valueOf(len+1);
                DiskManipulator.editLine(pointer, newLine);
                int block = FreeSpaceManager.getNFreeBlocksAt(13, 10)+1;
                String entry = "0 "+curr+" "+block;
                DiskManipulator.editLine(pointer+len+1, entry);
                newLine = curr+" 0";
                DiskManipulator.editLine(block, newLine);


            }
            else
            {
                int len = Integer.parseInt(DiskManipulator.getLine(pointer).split(" ")[1]);
                boolean correctPath = false;
                for (int i = 1; i <= len; i++)
                {
                    String[] line = DiskManipulator.getLine(pointer + i).split(" ");
                    if (line[0].equalsIgnoreCase("0") && line[1].equalsIgnoreCase(curr))
                    {
                        pointer = Integer.parseInt(line[2]);
                        iter++;
                        curr = arr[iter];
                        parent  = arr[iter-1];
                        correctPath = true;
                        break;
                    }
                }

                if(!correctPath)
                {
                    return false;
                }

            }
        }

        return false;
    }

    public abstract boolean deleteFile(String path);
    public boolean deleteFolder(String path)
    {


        return false;
    }
}
