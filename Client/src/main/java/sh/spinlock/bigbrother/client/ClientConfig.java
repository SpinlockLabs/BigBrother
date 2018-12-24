package sh.spinlock.bigbrother.client;

public class ClientConfig {
    public String broker;
    public int port = 5672;
    public String username = "bigbrother";
    public String password;

    public String buildAmqpUri() {
        return "amqp://" + username + ':' + password + '@' + broker + ':' + port + '/';
    }
}
