package levelMap;

import java.util.*;
import javafx.scene.paint.*;

public enum FloorTileType
{
	BLUE(Color.BLUE, Color.LIGHTBLUE, Color.PURPLE, Color.LIGHTBLUE),
	TECH(Color.BLACK, Color.LIGHTGRAY, Color.BLACK, Color.PALEVIOLETRED);

	FloorTileType(Color normal0, Color normal1, Color marked0, Color marked1)
	{
		normal = List.of(new Stop(0, normal0), new Stop(1, normal1));
		marked = List.of(new Stop(0, marked0), new Stop(1, marked1));
	}

	FloorTileType(List<Stop> normal, List<Stop> marked)
	{
		this.normal = normal;
		this.marked = marked;
	}

	public final List<Stop> normal;
	public final List<Stop> marked;
}