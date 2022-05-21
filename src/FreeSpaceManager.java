import java.math.BigInteger;

public class FreeSpaceManager
{
    private static String blocksState; //A hex string



    public static int getNFreeBlocksAt(int length)
    {
        return getNFreeBlocksAt(2,length);
    }
    public static int getNFreeBlocksAt(int start, int length)
    {
        if(length ==0) return -1;


        String bin = hexToBinary(blocksState);
        System.out.println(bin);
        int low = start, high, len =0;
        while(low<bin.length())
        {
            high = low;
            len = 0;
            while(high<bin.length() &&  bin.charAt(high) == '0')
            {
                len++;
                high++;

                if(len == length) return low;
            }
            low = high+1;
        }
        /*
        int i=start;
        for(; i<DiskManipulator.getNumOfBlocks()-length; i++)
        {
            String temp = bin.substring(start+i, start+i+length);
            BigInteger BDecimal = new BigInteger(temp,2);
            if(BDecimal != new BigInteger(String.valueOf(0),10))
                continue;
        }*/
        return -1;
    }


    public static void resetBlockState()
    {
        String bin = "11";
        int remainingZeros = DiskManipulator.getNumOfBlocks()- bin.length();
        for(int i=0; i<remainingZeros; i++)
        {
            bin+='0';
        }
        String hex = binaryToHex(bin);
        blocksState = hex;
        DiskManipulator.editLine(1,blocksState);
    }
    public static void modifyBlock(int index,char c)
    {
        if(index <2) return;
        String bin = hexToBinary(blocksState);
        bin = bin.substring(0,index)+c+bin.substring(index+1);

        blocksState = binaryToHex(bin);
        DiskManipulator.editLine(1,blocksState);
    }
    private static String hexToBinary(String hex)
    {
        return new BigInteger(hex, 16).toString(2);
    }


    private static String binaryToHex(String bin)
    {
        BigInteger BDecimal = new BigInteger(bin,2);
        return BDecimal.toString(16);
    }

    private static String expandBinary(String bin)
    {
        int remainingZeros = DiskManipulator.getNumOfBlocks()- bin.length();
        String temp = "";

        for(int i =0; i<remainingZeros; i++)
        {
            temp += "0";
        }

        return temp + bin;
    }
    public static String getBlocksState()
    {
        return blocksState;
    }
    public static void setBlocksState(String str)
    {
        blocksState =str;
    }

}
