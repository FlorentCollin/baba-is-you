import java.io.File;

public class test {
	
	public static void main(String[] args) {
		String[] s = new File("levels/saves").list();
		for(String i : s)
		{
			System.out.println(i);
		}
	}
}
