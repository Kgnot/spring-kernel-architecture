package org.example.microkernelspring.shared.application.kernel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PluginRegistry {

    void register(Plugin plugin);

    Optional<Plugin> findById(String pluginId);

    Optional<Plugin> findByName(String name);

    List<Plugin> findByType(PluginType type);

    List<Plugin> findAll();
}
