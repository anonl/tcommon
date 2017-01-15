package nl.weeaboo.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;

class PartManager {

    // -------------------------------------------------------------------------
    // * Attributes must be serialized manually
    // * Update reset method after adding/removing attributes
    // -------------------------------------------------------------------------
    private Scene scene;
    private final IdentityHashMap<IPart, Collection<Entity>> parts = new IdentityHashMap<IPart, Collection<Entity>>();
    // -------------------------------------------------------------------------

    public PartManager(Scene s) {
        this.scene = s;
    }

    void reset() {
        parts.clear();
    }

    void serialize() {
        // Parts mapping will be restored by Entity deserialization
    }

    void deserialize(Scene s) {
        reset();

        scene = s;

        // Parts mapping will be restored by Entity deserialization
    }

    public Iterable<Entity> entitiesWithPart(IPart p) {
        Collection<Entity> entities = parts.get(p);
        if (entities == null) {
            entities = Collections.emptyList();
        }
        return entities;
    }

    @Override
    public String toString() {
        return "PartManager[" + scene + "]";
    }

    public boolean contains(IPart p) {
        return parts.containsKey(p);
    }

    public void add(Entity e, IPart p) {
        Collection<Entity> list = parts.get(p);
        if (list == null) {
            list = new ArrayList<Entity>();
            parts.put(p, list);
        }
        list.add(e);
    }

    public void remove(Entity e, IPart p) {
        Collection<Entity> list = parts.get(p);
        if (list != null) {
            list.remove(e);
            if (list.isEmpty()) {
                parts.remove(p);
            }
        }
    }

}
