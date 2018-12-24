package sh.spinlock.bigbrother.client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sh.spinlock.bigbrother.client.abstraction.DataTarget;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class RabbitDataTarget extends DataTarget {
    private static final String PUSH_ROUTING_KEY = "push";
    private static final String STATUS_ROUTING_KEY = "status";
    private static final Logger LOG = LoggerFactory.getLogger(RabbitDataTarget.class);

    private Connection connection;
    private Channel channel;
    private String exchangeName;
    private String queueName;

    @Inject
    public RabbitDataTarget(ClientConfig config) {
        super(config);
    }

    @Override
    public void connect() throws Exception {
        ConnectionFactory connFactory = new ConnectionFactory();

        LOG.info("Connecting to {}@{}:{}", config.username, config.broker, config.port);
        connFactory.setHost(config.broker);
        connFactory.setUsername(config.username);
        connFactory.setPassword(config.password);
        connFactory.setPort(config.port);

        connection = connFactory.newConnection();
        channel = connection.createChannel();

        UUID uuid = UUID.randomUUID();
        exchangeName = uuid.toString();
        queueName = channel.queueDeclare().getQueue();
        LOG.debug("Declaring direct exchange {}", exchangeName);
        channel.exchangeDeclare(exchangeName, "direct", true);

        LOG.debug("Binding queue {} to exchange {}", queueName, exchangeName);
        channel.queueBind(queueName, exchangeName, PUSH_ROUTING_KEY);
        channel.queueBind(queueName, exchangeName, STATUS_ROUTING_KEY);
    }

    @Override
    public void disconnect() throws Exception {
        channel.close();
        connection.close();
    }

    @Override
    public boolean connected() {
        if (connection == null) {
            return false;
        }
        return connection.isOpen();
    }

    @Override
    public void status(String data) throws Exception {

    }

    @Override
    public void push(String data) throws Exception {
        LOG.debug("Pushing data {}", data);
        channel.basicPublish(exchangeName, PUSH_ROUTING_KEY, null, data.getBytes(StandardCharsets.UTF_8));
    }
}
