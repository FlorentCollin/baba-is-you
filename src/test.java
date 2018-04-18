import java.io.File;

public class test
{
    public static void main(String[] args) {
        File dir = new File("levels/saves");
        for(String element : dir.list())
        {
        	System.out.println(element);
        }
    }
}