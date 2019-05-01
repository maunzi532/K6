package levelMap;

import java.util.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;

public enum FloorTileType
{
	BLUE(Color.BLUE, Color.LIGHTBLUE, Color.PURPLE, Color.LIGHTBLUE, "Blue2.png", false, true, 1),
	GSL(Color.GREEN, Color.LIGHTBLUE, Color.PURPLE, Color.GREEN, "Green2.png", false, true, 2),
	TECH(Color.BLACK, Color.LIGHTGRAY, Color.BLACK, Color.PALEVIOLETRED, "Red2.png", false, true, 1),
	WALL(Color.BLACK, Color.LIGHTGRAY, Color.BLACK, Color.PALEVIOLETRED, "Gray2.png", true, false, 1);

	FloorTileType(Color normal0, Color normal1, Color marked0, Color marked1, String imageT, boolean blocked,
			boolean canMovementEnd,
			int moveCost)
	{
		this.imageT = imageT;
		image = new Image(imageT);
		this.blocked = blocked;
		this.canMovementEnd = canMovementEnd;
		this.moveCost = moveCost;
		normal = List.of(new Stop(0, normal0), new Stop(1, normal1));
		marked = List.of(new Stop(0, marked0), new Stop(1, marked1));
	}

	FloorTileType(List<Stop> normal, List<Stop> marked, String imageT, boolean blocked,
			boolean canMovementEnd, int moveCost)
	{
		this.normal = normal;
		this.marked = marked;
		this.imageT = imageT;
		image = new Image(imageT);
		this.blocked = blocked;
		this.canMovementEnd = canMovementEnd;
		this.moveCost = moveCost;
	}

	public final List<Stop> normal;
	public final List<Stop> marked;
	public final String imageT;
	public final Image image;
	public final boolean blocked;
	public final boolean canMovementEnd;
	public final int moveCost;
}