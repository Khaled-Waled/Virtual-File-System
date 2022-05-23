import java.util.Objects;

public abstract class Allocator
{
    public abstract boolean createFile(String path, int size);
    public boolean createFolder(String path)
    {
        int MAX_ENTRIES = DiskManipulator.MAX_Entries;
        String[] arr = path.split("/");
        int arrLen = arr.length;
        int iter = 1;
        String curr = arr[iter];
        int STARTPOS = 2;
        int pointer = STARTPOS;

        while(iter < arrLen)
        {
            if(iter == arrLen-1)
            {
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
                int block = FreeSpaceManager.getNFreeBlocksAt(pointer+len, MAX_ENTRIES);
                String entry = "0 "+curr+" "+block;
                DiskManipulator.editLine(pointer+len+1, entry);
                newLine = curr+" 0";
                DiskManipulator.editLine(block, newLine);
                for(int i=block+1; i< Math.min(DiskManipulator.getNumOfBlocks(), block+MAX_ENTRIES); i++)
                {
                    DiskManipulator.editLine(i,".");
                }


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
        int MAX_ENTRIES = DiskManipulator.MAX_Entries;
        String[] arr = path.split("/");
        int arrLen = arr.length;
        int iter = 1;
        String curr = arr[iter];
        int STARTPOS = 2;
        int pointer = STARTPOS;

        while(iter < arrLen)
        {
            if(iter == arrLen-1)
            {
                String[] head = DiskManipulator.getLine(pointer).split(" ");
                int len = Integer.parseInt(head[1]);
                int s = 0;
                //int count = len;
                for (int i = 1; s<len; i++)
                {
                    String[] line = DiskManipulator.getLine(pointer + i).split(" ");
                    if(Objects.equals(line[0], "."))
                    {
                        continue;
                    }
                    if (line[0].equalsIgnoreCase("0") && line[1].equalsIgnoreCase(curr))
                    {
                        //Delete entry
                        //save address before deleting
                        int block = Integer.parseInt(line[2]);
                        int oldPointer = pointer;

                        //move to the correct position
                        pointer = block;
                        line = DiskManipulator.getLine(pointer).split(" ");
                        int contentSize = Integer.parseInt(line[1]);
                        int j=1;
                        //Delete children entries
                        for (; j<=contentSize; j++)
                        {
                            String[] sub_line = DiskManipulator.getLine(pointer + j).split(" ");
                            if (sub_line[0].equalsIgnoreCase("0"))
                            {
                                String newPath = path+'/'+sub_line[1];
                                System.out.println("recursively Deleting folder At: "+ newPath);
                                //Recursive call to delete inner folders
                                if(!deleteFolder(newPath)) System.out.println("cant recursively delete "+ newPath);
                                    //return false;
                            }
                            else if(sub_line[0].equalsIgnoreCase("1"))
                            {
                                String newPath = path+'/'+sub_line[1];
                                System.out.println("recursively Deleting folder At: "+ newPath);
                                DiskManipulator.allocator.deleteFile(newPath);
                            }
                            DiskManipulator.editLine(pointer+j, "");
                        }
                        //delete dots (content)
                        while(Objects.equals(DiskManipulator.getLine(pointer + j), "."))
                        {
                            DiskManipulator.editLine(pointer+j, "");
                            j++;
                        }
                        //delete directory segment header
                        DiskManipulator.editLine(pointer, "");

                        //decrement entries value of the parent
                        String newHead = head[0]+" "+ String.valueOf(len-1);
                        DiskManipulator.editLine(oldPointer, newHead);

                        //delete entry at parent
                        DiskManipulator.editLine(oldPointer+i, ".");
                        return true;
                    }
                    s++;
                }
                return false;
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
}
