package nl.weeaboo.common;

/**
 * Base class for type-safe string-based identifiers. For each class of identifier, create a subclass of
 * {@llink AbstractId}.
 */
public abstract class AbstractId {

    private final String id;

    public AbstractId(String id) {
        this.id = Checks.checkNotNull(id);
    }

    public final String getId() {
        return id;
    }

    @Override
    public final int hashCode() {
        return id.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || obj.getClass() != getClass()) {
            return false; // Null or wrong type
        }

        AbstractId other = (AbstractId)obj;
        return id.equals(other.id);
    }

    @Override
    public String toString() {
        return id;
    }

}
