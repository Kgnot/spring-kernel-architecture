package org.example.microkernelspring.shared.kernel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MicroKernel {

    private final PluginRegistry registry;
    private final KernelContext context;

    public MicroKernel(List<Plugin> pluginsDescubiertos, PluginRegistry registry, KernelContext context) {
        this.registry = registry;
        log.info("MicroKernel pluginsDescubiertos: {}", pluginsDescubiertos);
        this.context = context;
        pluginsDescubiertos.forEach(p -> {
            registry.register(p);
            p.onLoad(context);
        });
    }

    public PluginRegistry getRegistry() {
        return registry;
    }

}
