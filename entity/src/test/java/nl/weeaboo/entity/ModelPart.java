package nl.weeaboo.entity;

class ModelPart extends Part {

	private static final long serialVersionUID = 2L;

	private int x, y, z;

	public ModelPart() {
	}
	public ModelPart(int x, int y, int z) {
		this();

		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getZ() {
		return z;
	}

	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setZ(int z) {
		this.z = z;
	}

}
