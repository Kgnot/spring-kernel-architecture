package org.example.microkernelspring.shared.kernel;

public interface KernelContext {
    void publish(String event, Object data);
    void subscribe(String event, EventListener listener);
    <T> T getService(Class<T> serviceClass);
    <T> void registerService(Class<T> contract, T implementation);
}
