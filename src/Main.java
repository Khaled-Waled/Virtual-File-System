public class Main
{
    public static void main(String[] args)
    {
        new DiskManipulator("./structure.vfs", 50);
        //DiskManipulator.formatDisk("./structure.vfs",200);


        DiskManipulator.editLine(5,"asdasd");
        int x = FreeSpaceManager.getNFreeBlocksAt(49,4);

        System.out.println("x= "+ x);

        /*while(true)
        {
            Terminal.chooseCommandAction();
        }*/

    }
}
