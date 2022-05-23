import java.util.Objects;

public class ContiguousAllocator extends Allocator
{

    @Override
    public boolean createFile(String path, int size)
    {
        String[] arr = path.split("/");
        int arrLen = arr.length;
        int iter = 1;
        String curr = arr[iter];
        int STARTPOS = 2;
        int pointer = STARTPOS;

        while (iter < arrLen)
        {
            if (iter == arrLen - 1)
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

                //create file entry
                String newLine = head[0] + " " + String.valueOf(len + 1);
                DiskManipulator.editLine(pointer, newLine);
                int block = FreeSpaceManager.getNFreeBlocksAt(pointer + len, size);
                String entry = "1 " + curr + " " + block + " " + size;
                DiskManipulator.editLine(pointer + len + 1, entry);

                //Write file content
                for (int i = block; i < Math.min(DiskManipulator.getNumOfBlocks(), block + size); i++)
                {
                    DiskManipulator.editLine(i, "DATA OF: " + curr);
                }
                return true;
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

                if (!correctPath)
                {
                    return false;
                }
            }
        }

        return false;
    }


    @Override
    public boolean deleteFile(String path)
    {
        String[] arr = path.split("/");
        int arrLen = arr.length;
        int iter = 1;
        String curr = arr[iter];
        int STARTPOS = 2;
        int pointer = STARTPOS;

        while (iter < arrLen)
        {
            if (iter == arrLen - 1)
            {
                String[] head = DiskManipulator.getLine(pointer).split(" ");
                int len = Integer.parseInt(head[1]);
                //declare variable to skip dots while searching
                int s = 0;
                //int count = len;
                for (int i = 1; s < len; i++)
                {
                    String[] line = DiskManipulator.getLine(pointer + i).split(" ");
                    if (Objects.equals(line[0], "."))
                    {
                        continue;
                    }
                    if (line[0].equalsIgnoreCase("1") && line[1].equalsIgnoreCase(curr))
                    {
                        //Delete entry
                        //save address before deleting
                        int block = Integer.parseInt(line[2]);
                        //save size before deleting
                        int size = Integer.parseInt(line[3]);
                        //delete entry at parent
                        DiskManipulator.editLine(pointer + i, ".");

                        //decrement entries value of the parent
                        String newHead = head[0] + " " + String.valueOf(len - 1);
                        DiskManipulator.editLine(pointer, newHead);

                        //move to the correct position
                        pointer = block;
                        //delete dots (content)
                        for (int j = 0; j < size; j++)
                        {
                            DiskManipulator.editLine(pointer + j, "");
                        }
                        return true;
                    }
                    s++;
                }
                return false;
            }
        }
        return false;
    }
}
