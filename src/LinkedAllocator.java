public class LinkedAllocator extends Allocator
{

    @Override
    public boolean createFile(String path, int size)
    {
        return false;
    }


    @Override
    public boolean deleteFile(String path)
    {
        return false;
    }
}
