package sh.spinlock.bigbrother.client;

public class Main {
    private static Client client;

    public static void main(String[] args) {
        client = new Client();
        client.run();
    }
}
