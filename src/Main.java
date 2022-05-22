public class Main
{
    public static void main(String[] args)
    {

        new DiskManipulator("./structure.vfs");
        //DiskManipulator.formatDisk("./structure.vfs",200);


        while(true)
        {
            Terminal.chooseCommandAction();
        }
    }
}
