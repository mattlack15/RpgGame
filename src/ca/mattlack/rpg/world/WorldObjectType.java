package ca.mattlack.rpg.world;

import ca.mattlack.rpg.util.Registry;
import ca.mattlack.rpg.util.Serializer;

import java.util.function.Function;

public class WorldObjectType
{

    public static final Registry<String, WorldObjectType> REGISTRY = new Registry<>(); // The registry of all world object types.

    private final String identifier; // The identifier of this world object type.
    private final Function<Serializer, WorldObject> deserializer; // The deserializer for this world object type.
    private final Function<WorldObject, Serializer> serializer; // The serializer for this world object type.

    public WorldObjectType(String identifier, Function<Serializer, WorldObject> deserializer, Function<WorldObject, Serializer> serializer)
    {
        this.identifier = identifier;
        this.deserializer = deserializer;
        this.serializer = serializer;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public Function<Serializer, WorldObject> getDeserializer()
    {
        return deserializer;
    }

    public Function<WorldObject, Serializer> getSerializer()
    {
        return serializer;
    }

    public WorldObjectType register() {
        return REGISTRY.register(identifier, this);
    }
}
