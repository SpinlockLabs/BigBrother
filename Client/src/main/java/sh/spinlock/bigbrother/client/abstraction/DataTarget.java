package sh.spinlock.bigbrother.client.abstraction;

import sh.spinlock.bigbrother.client.ClientConfig;

import javax.inject.Inject;

public abstract class DataTarget {
    protected final ClientConfig config;

    @Inject
    public DataTarget(ClientConfig config) {
        this.config = config;
    }

    public abstract void connect() throws Exception;
    public abstract void disconnect() throws Exception;
    public abstract boolean connected();

    public abstract void status(String data) throws Exception;
    public abstract void push(String data) throws Exception;
}
