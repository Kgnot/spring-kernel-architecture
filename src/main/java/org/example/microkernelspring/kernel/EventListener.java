package org.example.microkernelspring.kernel;

@FunctionalInterface
public interface EventListener {
    void onEvent(String event, Object data);
}
