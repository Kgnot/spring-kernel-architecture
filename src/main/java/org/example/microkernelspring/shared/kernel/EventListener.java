package org.example.microkernelspring.shared.kernel;

@FunctionalInterface
public interface EventListener {
    void onEvent(String event, Object data);
}
