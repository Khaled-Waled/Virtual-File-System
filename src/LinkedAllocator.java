public class LinkedAllocator extends Allocator
{


    final int nodeSize = 5;
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


                String newLine = head[0] + " " + String.valueOf(len + 1);
                DiskManipulator.editLine(pointer, newLine);
                int blockInd = FreeSpaceManager.getNFreeBlocksAt(2,nodeSize + 1);
                String entry = "1 " + curr + " " + blockInd;
                DiskManipulator.editLine(pointer + len + 1, entry);

                while(size > 0){
                    pointer = blockInd;
                    //Write file content
                    for (int i = pointer; i < Math.min(DiskManipulator.getNumOfBlocks(), pointer + nodeSize); i++) {
                        DiskManipulator.editLine(i + 1, "DATA OF: " + curr);
                    }
                    size -= nodeSize;
                    if(size > 0)
                        blockInd = FreeSpaceManager.getNFreeBlocksAt(2, nodeSize + 1);
                    else
                        blockInd = -1;
                    entry = curr + " " + blockInd;
                    DiskManipulator.editLine(pointer, entry);
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

        while (iter < arrLen) {
            if (iter == arrLen - 1) {
                String[] head = DiskManipulator.getLine(pointer).split(" ");
                int len = Integer.parseInt(head[1]);
                boolean found = false;
                int blockInd = 0;
                int i,s=0;
                for (i = 1; s<len; i++)
                {
                    String[] line = DiskManipulator.getLine(pointer + i).split(" ");
                    if(line[0].equals("."))
                    {
                        continue;
                    }
                    if (line[0].equalsIgnoreCase("1") && line[1].equalsIgnoreCase(curr)) {
                        //found
                        blockInd = Integer.parseInt(line[2]);
                        found = true;
                        break;
                    }
                    s++;
                }
                if (!found)
                    return false;

                //Delete file entry
                String newLine = head[0] + " " + String.valueOf(len - 1);
                DiskManipulator.editLine(pointer, newLine);

                String[] Line = DiskManipulator.getLine(pointer + i).split(" ");
                DiskManipulator.editLine(pointer + i, ".");

                while(pointer != -1){
                    pointer = blockInd;
                    for (int j = pointer; j < Math.min(DiskManipulator.getNumOfBlocks(), pointer + nodeSize); j++) {
                        DiskManipulator.editLine(j + 1, "");
                    }
                    String[] line = DiskManipulator.getLine(pointer).split(" ");
                    blockInd = Integer.parseInt(line[1]);
                    DiskManipulator.editLine(pointer,"");
                    pointer = blockInd;
                }
                return true;


            } else {
                int len = Integer.parseInt(DiskManipulator.getLine(pointer).split(" ")[1]);
                boolean correctPath = false;
                for (int i = 1; i <= len; i++) {
                    String[] line = DiskManipulator.getLine(pointer + i).split(" ");
                    if (line[0].equalsIgnoreCase("0") && line[1].equalsIgnoreCase(curr)) {
                        pointer = Integer.parseInt(line[2]);
                        iter++;
                        curr = arr[iter];
                        correctPath = true;
                        break;
                    }
                }

                if (!correctPath) {
                    return false;
                }
            }
        }
        return false;
    }
}
