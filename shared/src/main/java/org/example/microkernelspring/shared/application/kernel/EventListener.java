package org.example.microkernelspring.shared.application.kernel;

@FunctionalInterface
public interface EventListener {
    void onEvent(String event, Object data);
}
