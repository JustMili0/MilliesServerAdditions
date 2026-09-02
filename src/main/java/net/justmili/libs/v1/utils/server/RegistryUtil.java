package net.justmili.libs.v1.utils.server;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

import java.util.Optional;

public class RegistryUtil {

    public static <T> Registry<T> lookup(MinecraftServer server, ResourceKey<Registry<T>> registryResource) {
        return server.registryAccess().lookupOrThrow(registryResource);
    }
    
    public static <T> Registry<T> lookup(ServerLevel level, ResourceKey<Registry<T>> registryResource) {
        return lookup(level.getServer(), registryResource);
    }

    public static <T> Optional<Holder.Reference<T>> get(MinecraftServer server, ResourceKey<Registry<T>> registryResource, ResourceKey<T> key) {
        return lookup(server, registryResource).get(key);
    }

    public static <T> Optional<Holder.Reference<T>> get(ServerLevel level, ResourceKey<Registry<T>> registryResource, ResourceKey<T> key) {
        return get(level.getServer(), registryResource, key);
    }
    
    public static <T> Holder.Reference<T> getOrThrow(MinecraftServer server, ResourceKey<Registry<T>> registryResource, ResourceKey<T> key) {
        return lookup(server, registryResource).getOrThrow(key);
    }

    public static <T> Holder.Reference<T> getOrThrow(ServerLevel level, ResourceKey<Registry<T>> registryResource, ResourceKey<T> key) {
        return getOrThrow(level.getServer(), registryResource, key);
    }
}
