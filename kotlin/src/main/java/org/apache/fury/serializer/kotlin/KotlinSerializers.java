package org.apache.fury.serializer.kotlin;

import org.apache.fury.Fury;
import org.apache.fury.resolver.ClassResolver;
import org.apache.fury.serializer.Serializer;
import org.apache.fury.serializer.SerializerFactory;

@SuppressWarnings({"rawtypes", "unchecked"})
public class KotlinSerializers {
    public static void registerSerializers(Fury fury) {
        ClassResolver resolver = setSerializerFactory(fury);
        resolver.registerSerializer(kotlin.collections.ArrayDeque.class, new KotlinArrayDequeSerializer(fury, kotlin.collections.ArrayDeque.class));
        resolver.register(kotlin.collections.ArrayDeque.class);
    }

    private static ClassResolver setSerializerFactory(Fury fury) {
        ClassResolver resolver = fury.getClassResolver();
        KotlinDispatcher dispatcher = new KotlinDispatcher();
        SerializerFactory factory = resolver.getSerializerFactory();
        if (factory != null) {
            SerializerFactory newFactory = (f, cls) -> {
                Serializer<?> serializer = factory.createSerializer(f, cls);
                if (serializer == null) {
                    serializer = dispatcher.createSerializer(f, cls);
                }
                return serializer;
            };
            resolver.setSerializerFactory(newFactory);
        } else {
            resolver.setSerializerFactory(dispatcher);
        }
        return resolver;
    }
}
