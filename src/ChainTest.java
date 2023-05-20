import java.util.Scanner;
public class ChainTest {
    public static void main(String[] args) throws Exception {
        Scanner scn = new Scanner(System.in);
        System.out.print("Enter Name: ");
        String uname = scn.nextLine();
        System.out.print("Enter seed: ");
        Integer seed = Integer.valueOf(scn.nextLine());
        scn.close();
        Chain chain = new Chain(seed,uname);
    }
}