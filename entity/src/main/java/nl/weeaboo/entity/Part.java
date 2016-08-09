package nl.weeaboo.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Part implements IPart {

    private static final long serialVersionUID = 1L;

    private transient boolean attached;

    @Override
    public String toDetailedString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("(");
		int t = 0;
		for (Field field : getClass().getDeclaredFields()) {
			int modifiers = field.getModifiers();
			if (Modifier.isStatic(modifiers)) {
				continue;
			}

			if (t > 0) sb.append(", ");

			sb.append(field.getName()).append("=");
			try {
				field.setAccessible(true);
				Object val = field.get(this);
				sb.append(val);
			} catch (Exception e) {
				EntityLog.d("Exception while trying to access Part." + field.getName() + " using reflection", e);
				sb.append("?");
			}
			t++;
		}
		sb.append(")");
		return sb.toString();
	}

    @Override
    public void onAttached(Scene scene) {
        attached = true;
    }

    @Override
    public void onDetached(Scene scene) {
        attached = false;
    }

    public boolean isAttached() {
        return attached;
    }

    @Override
    public void handleSignal(ISignal signal) {
    }

}
