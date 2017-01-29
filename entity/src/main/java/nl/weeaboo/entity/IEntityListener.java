package nl.weeaboo.entity;

public interface IEntityListener {

    /**
     * Called when a new entity is created.
     */
    void onEntityCreated(Entity e);

    /**
     * Called when an entity is attached to a scene.
     */
    void onEntityAttached(Scene s, Entity e);

    /**
     * Called when an entity is detached from a scene.
     */
    void onEntityDetached(Scene s, Entity e);

    /**
     * Called when an entity is destroyed.
     */
    void onEntityDestroyed(Entity e);

}
