package net.justmili.servertweaks.content.abilities.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.justmili.servertweaks.content.abilities.type.AnyType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class TypeCodecs {
    public static <T extends AnyType> Codec<T> forType(String typeName, Function<Identifier, T> lookup) {
        return Identifier.CODEC.comapFlatMap(id -> {
            var value = lookup.apply(id);
            return value != null ? DataResult.success(value) : DataResult.error(() -> "Could not find " + typeName + " " + id + "!");
        }, AnyType::getId);
    }

    public static <T extends AnyType> StreamCodec<ByteBuf, T> streamForType(Function<Identifier, T> lookup) {
        return Identifier.STREAM_CODEC.map(lookup, AnyType::getId);
    }

    public static <T extends AnyType> Codec<Set<T>> setOf(Codec<T> elementCodec) {
        return elementCodec.listOf().xmap(HashSet::new, ArrayList::new);
    }

    public static <T extends AnyType> StreamCodec<ByteBuf, Set<T>> streamSetOf(StreamCodec<ByteBuf, T> elementCodec) {
        return ByteBufCodecs.collection(HashSet::new, elementCodec);
    }
}