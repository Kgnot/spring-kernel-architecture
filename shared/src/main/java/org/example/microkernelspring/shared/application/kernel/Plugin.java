package org.example.microkernelspring.shared.application.kernel;

public interface Plugin {
    String getId();
    PluginType getType();
    void onLoad(KernelContext context);
    void onUnload(KernelContext context);
}
