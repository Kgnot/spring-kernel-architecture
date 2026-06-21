package org.example.microkernelspring.kernel;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.microkernelspring.shared.application.kernel.KernelContext;
import org.example.microkernelspring.shared.application.kernel.Plugin;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MicroKernel {

    @Getter
    private final InMemoryPluginRegistry registry;
    private final KernelContext context;

    public MicroKernel(List<Plugin> pluginsDescubiertos, InMemoryPluginRegistry registry, KernelContext context) {
        this.registry = registry;
        log.info("MicroKernel pluginsDescubiertos: {}", pluginsDescubiertos);
        this.context = context;
        pluginsDescubiertos.forEach(p -> {
            registry.register(p);
            p.onLoad(context);
        });
    }



}
