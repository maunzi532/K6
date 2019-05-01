package levelMap;

import javafx.scene.image.*;

public enum FloorTileType
{
	BLUE("Blue2.png", false, true, 1),
	GSL("Green2.png", false, true, 2),
	TECH("Red2.png", false, true, 1),
	WALL("Gray2.png", true, false, 1);

	FloorTileType(String imageT, boolean blocked, boolean canMovementEnd, int moveCost)
	{
		this.imageT = imageT;
		image = new Image(imageT);
		this.blocked = blocked;
		this.canMovementEnd = canMovementEnd;
		this.moveCost = moveCost;
	}

	public final String imageT;
	public final Image image;
	public final boolean blocked;
	public final boolean canMovementEnd;
	public final int moveCost;
}