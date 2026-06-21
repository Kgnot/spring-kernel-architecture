package org.example.microkernelspring.kernel;

import org.example.microkernelspring.shared.application.kernel.Plugin;
import org.example.microkernelspring.shared.application.kernel.PluginRegistry;
import org.example.microkernelspring.shared.application.kernel.PluginType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryPluginRegistry implements PluginRegistry {

    private final Map<String, Plugin> loadedPlugins = new ConcurrentHashMap<>();

    public void register(Plugin plugin) {
        loadedPlugins.put(plugin.getId(), plugin);
    }

    public Optional<Plugin> findById(String pluginId) {
        return Optional.ofNullable(loadedPlugins.get(pluginId));
    }

    public Optional<Plugin> findByName(String name) {
        return Optional.ofNullable(loadedPlugins.get(name));
    }

    public List<Plugin> findByType(PluginType type) {
        return loadedPlugins.values()
                .stream()
                .filter(plugin -> plugin.getType() == type)
                .toList();
    }

    public List<Plugin> findAll() {
        return loadedPlugins.values()
                .stream()
                .toList();
    }
}