package CodeDraw;

public interface EventHandler<TSender, TArgs> {
	void handle(TSender sender, TArgs args);
}
