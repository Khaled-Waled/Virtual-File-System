import java.util.ArrayList;

public class IndexedAllocator extends Allocator {

    String fillBLocks(String name, int size) {
        int Disk = 3;
        ArrayList<String> pointers = new ArrayList<>();
        int bounds = DiskManipulator.getNumOfBlocks();
        for (int Data = 0; Data < size && Disk < bounds; Disk++) {
            if (DiskManipulator.getLine(Disk) == null) {
                DiskManipulator.editLine(Disk, name);
                pointers.add(String.valueOf(Disk));
                Data++;
            }
        }
        return String.join(" ", pointers);
    }

    boolean emptyBLocks(String ind) {
        String[] indexarray = ind.split(" ");
        for (int j = 0; j < indexarray.length; j++) {
            int block = Integer.parseInt(indexarray[j]);
            DiskManipulator.editLine(block, "");
        }
        return true;
    }

    @Override
    public boolean createFile(String path, int size) {
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
                for (int i = 1; i <= len; i++) {
                    String[] line = DiskManipulator.getLine(pointer + i).split(" ");
                    if (line[0].equalsIgnoreCase("1") && line[1].equalsIgnoreCase(curr)) {
                        //already created
                        return false;
                    }
                }

                //create file entry
                String newLine = head[0] + " " + String.valueOf(len + 1);
                DiskManipulator.editLine(pointer, newLine);

                int nFreeBlocksAt = FreeSpaceManager.getNFreeBlocksAt(pointer + len, 1);
                String entry = "1 " + curr + " " + nFreeBlocksAt;
                DiskManipulator.editLine(pointer + len + 1, entry);

                newLine = fillBLocks(curr, size);
                DiskManipulator.editLine(nFreeBlocksAt, newLine);
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


    @Override
    public boolean deleteFile(String path) {
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
                int i;
                for (i = 1; i <= len; i++) {
                    String[] line = DiskManipulator.getLine(pointer + i).split(" ");
                    if (line[0].equalsIgnoreCase("1") && line[1].equalsIgnoreCase(curr)) {
                        //found
                        found = true;
                        break;
                    }
                }
                if (!found)
                    return false;

                //Delete file entry
                String[] newLine = DiskManipulator.getLine(pointer + i).split(" ");
                DiskManipulator.editLine(pointer + i, ".");
                int start = Integer.parseInt(newLine[newLine.length - 1]);
                String indexarray = DiskManipulator.getLine(start);
                DiskManipulator.editLine(start, ".");
                emptyBLocks(indexarray);


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
