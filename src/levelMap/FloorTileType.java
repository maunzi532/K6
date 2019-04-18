package levelMap;

import java.util.*;
import javafx.scene.paint.*;

public enum FloorTileType
{
	BLUE(Color.BLUE, Color.LIGHTBLUE, Color.PURPLE, Color.LIGHTBLUE, false, true, 1),
	GSL(Color.GREEN, Color.LIGHTBLUE, Color.PURPLE, Color.GREEN, false, true, 2),
	TECH(Color.BLACK, Color.LIGHTGRAY, Color.BLACK, Color.PALEVIOLETRED, false, true, 1);

	FloorTileType(Color normal0, Color normal1, Color marked0, Color marked1, boolean blocked, boolean canMovementEnd,
			int moveCost)
	{
		this.blocked = blocked;
		this.canMovementEnd = canMovementEnd;
		this.moveCost = moveCost;
		normal = List.of(new Stop(0, normal0), new Stop(1, normal1));
		marked = List.of(new Stop(0, marked0), new Stop(1, marked1));
	}

	FloorTileType(List<Stop> normal, List<Stop> marked, boolean blocked, boolean canMovementEnd, int moveCost)
	{
		this.normal = normal;
		this.marked = marked;
		this.blocked = blocked;
		this.canMovementEnd = canMovementEnd;
		this.moveCost = moveCost;
	}

	public final List<Stop> normal;
	public final List<Stop> marked;
	public final boolean blocked;
	public final boolean canMovementEnd;
	public final int moveCost;
}