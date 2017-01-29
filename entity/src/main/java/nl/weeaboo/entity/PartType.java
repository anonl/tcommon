package nl.weeaboo.entity;

import java.io.Serializable;

/**
 * Stores information about a part registered through a {@link PartRegistry}.
 */
public final class PartType<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int id;
    private final String name;
    private final Class<T> partInterface;

    /**
     * @param id Unique part type ID.
     * @param name Part type name.
     * @param partInterface Implementation type for parts belonging to this part type.
     */
    public PartType(int id, String name, Class<T> partInterface) {
        this.id = id;
        this.name = name;
        this.partInterface = partInterface;
    }

    /**
     * Casts a part instance to the part interface used by this part type.
     * @throws ClassCastException If the part instance isn't compatible with this part type.
     */
    public T cast(IPart part) throws ClassCastException {
        return partInterface.cast(part);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof PartType)) {
            return false;
        }

        PartType<?> pt = (PartType<?>)obj;
        return id == pt.id
            && name.equals(pt.name)
            && partInterface == pt.partInterface;
    }

    /**
     * The unique part type id.
     */
    public int getId() {
        return id;
    }

    /**
     * The Part type's name.
     */
    public String getName() {
        return name;
    }

    /**
     * The implementation type for parts belonging to this part type.
     */
    public Class<T> getPartInterface() {
        return partInterface;
    }

}