package org.example.microkernelspring.kernel;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PluginRegistry {

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