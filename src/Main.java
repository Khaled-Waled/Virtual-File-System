public class Main
{
    public static void main(String[] args)
    {

        new DiskManipulator();
        //DiskManipulator.formatDisk("./structure.vfs",200);

        new UserManager();
        new CapManager();
        while(true)
        {
            Terminal.chooseCommandAction();
        }




    }
}
