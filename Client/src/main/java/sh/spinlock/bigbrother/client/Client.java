package sh.spinlock.bigbrother.client;

import dagger.Provides;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sh.spinlock.bigbrother.client.abstraction.DataTarget;
import sh.spinlock.bigbrother.core.Configuration;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Client implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(Client.class);
    private static final ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(4);

    private ClientConfig config;
    private DataTarget target;

    public void run() {
        LOG.info("Starting client");

        try {
            config = Configuration.read("client.json", ClientConfig.class);
        } catch (IOException e) {
            LOG.error("Failed to read client configuration", e);
            throw new RuntimeException("Failed to read client configuration", e);
        }

        target = new RabbitDataTarget(config);

        threadPool.scheduleWithFixedDelay(() -> {
            try {
                if (!this.target.connected()) {
                    this.target.connect();
                }
            } catch (Exception e) {
                LOG.error("Failed to connect to broker", e);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Provides
    public ClientConfig getConfig() {
        return config;
    }

    @Provides
    public DataTarget getDataTarget() {
        return target;
    }
}
