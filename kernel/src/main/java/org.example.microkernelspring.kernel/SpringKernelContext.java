package org.example.microkernelspring.kernel;

import lombok.extern.slf4j.Slf4j;
import org.example.microkernelspring.shared.application.kernel.EventListener;
import org.example.microkernelspring.shared.application.kernel.KernelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class SpringKernelContext implements KernelContext {

    private final ApplicationContext applicationContext;
    private final Map<Class<?>, Object> dynamicServices = new ConcurrentHashMap<>();
    private final Map<String, List<EventListener>> listeners = new ConcurrentHashMap<>();

    public SpringKernelContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void publish(String event, Object data) {
        listeners.getOrDefault(event, List.of()).forEach(l -> l.onEvent(event, data));
    }

    @Override
    public void subscribe(String event, EventListener listener) {
        log.info("Subscribing listener to {}", event);
        listeners.computeIfAbsent(event, k -> new ArrayList<>()).add(listener);
    }

    @Override
    public <T> T getService(Class<T> serviceClass) {
        Object dyn = dynamicServices.get(serviceClass);
        return dyn != null ? (T) dyn : applicationContext.getBean(serviceClass);
    }

    @Override
    public <T> void registerService(Class<T> contract, T implementation) {
        dynamicServices.put(contract, implementation);
    }
}
