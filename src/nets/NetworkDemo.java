package nets;

import java.io.IOException;

public class NetworkDemo {
	
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to Network Demo!");
        
        Network n = Network.randNetwork(1, 1, 3, 2, 2, -1, 1);
        
        n.print();
        n.resetValues();
        
        double[] in = {1.0};
        double[] out = null;
        try {
			out = n.tick(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        for(int i = 0; i < 1; i++){
        	System.out.println(out[i]);
        }

        Network n2 = Network.mutateNetwork(n, 0.25, 0.25);
        try {
			out = n2.tick(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        for(int i = 0; i < 1; i++){
        	System.out.println(out[i]);
        }
        
    }
}
