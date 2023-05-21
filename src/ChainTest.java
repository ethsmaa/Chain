import java.util.Random;
import java.util.Scanner;
public class ChainTest {
    public static void main(String[] args) throws Exception {
    	// Going to take to Chain.java class
        Scanner scn = new Scanner(System.in);
        System.out.print("Enter Name: ");
        String uname = scn.nextLine();
        System.out.print("Enter seed (If you leave it blank, a random seed is generated): ");
        String seedInput = scn.nextLine();
        
        // Going to write to enigma console
        int seed;
        if (seedInput.isEmpty()) {
            seed = new Random().nextInt(9000) + 1000; // 1000 ile 9999 arasında rastgele bir sayı oluştur
            System.out.println("Oluşturulan rastgele tohum: " + seed);
        } else {
            try {
                seed = Integer.parseInt(seedInput);
            } catch (NumberFormatException e) {
            	
            	System.out.println("Hatalı tohum değeri girildi : " + seedInput);
            	seed = new Random().nextInt(9000) + 1000; // 1000 ile 9999 arasında rastgele bir sayı oluştur
            	System.out.println("Oluşturulan rastgele tohum: " + seed);
            }
        }
        scn.close();
        Chain chain = new Chain(seed,uname);
    }
}