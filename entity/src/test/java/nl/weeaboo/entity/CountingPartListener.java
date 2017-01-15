package nl.weeaboo.entity;

class CountingPartListener implements IPartListener {

    public int attached;
    public int detached;
    public int propertyChanges;

    @Override
    public void onPartAttached(Entity e, IPart p) {
        attached++;
    }

    @Override
    public void onPartDetached(Entity e, IPart p) {
        detached++;
    }

    @Override
    public void onPartPropertyChanged(Entity e, IPart p, String propertyName, Object newValue) {
        propertyChanges++;
    }

}