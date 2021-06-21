package CodeDraw.TextFormat;

public enum FontWeight {
	LIGHT(0.75f), NORMAL(1.0f), BOLD(2.0f), BOLDER(2.5f);

	private final float weight;
	FontWeight(float weight){
		this.weight = weight;
	}

	public float getWeight() {
		return weight;
	}
}
