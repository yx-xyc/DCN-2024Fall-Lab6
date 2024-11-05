import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        TcpProtocol tcp = new TcpProtocol();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                tcp.processEvent(line.trim());
            }
        } catch (Exception e) {
            System.err.println("Error reading input: " + e.toString());
        }
    }
}
