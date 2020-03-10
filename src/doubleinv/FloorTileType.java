package doubleinv;

import javafx.scene.image.*;

public enum FloorTileType
{
	BLUE("BLUE_Tile.png", false, true, 1),
	GSL("GSL_Tile.png", false, true, 2),
	TECH("RED_Tile.png", false, true, 1),
	WALL("WALL_Tile.png", true, false, 1);

	//Hex height same as Quad height

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