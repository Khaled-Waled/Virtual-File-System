public abstract class Allocator
{
    public abstract boolean createFile(String path, int size);
    public boolean createFolder(String dirName, int block)
    {
        String line = dirName+" 0";
        DiskManipulator.editLine(block,line);
        return true;
    }

    public abstract boolean deleteFile(String path);
    public boolean deleteFolder(String path)
    {


        return false;
    }
}
