package nl.weeaboo.io;

import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * This is the interface version of the magic 'writeReplace' method used by Java's {@link ObjectOutputStream}.
 */
public interface IWriteReplaceSerializable extends Serializable {

    /**
     * This is the interface version of the magic 'writeReplace' method used by Java's {@link ObjectOutputStream}.
     *
     * @throws ObjectStreamException If the object can't be written.
     */
    Object writeReplace() throws ObjectStreamException;

}
