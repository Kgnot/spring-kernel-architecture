package org.example.microkernelspring.kernel;

public interface Plugin {
    String getId();
    PluginType getType();
    void onLoad(KernelContext context);
    void onUnload(KernelContext context);
}
