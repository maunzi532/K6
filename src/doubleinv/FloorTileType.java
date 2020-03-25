package doubleinv;

public enum FloorTileType
{
	BLUE("floortile.blue", false, true, 1),
	GSL("floortile.gsl", false, true, 2),
	TECH("floortile.tech", false, true, 1),
	WALL("floortile.wall", true, false, 1);

	//Hex height same as Quad height

	FloorTileType(String image, boolean blocked, boolean canMovementEnd, int moveCost)
	{
		this.image = image;
		this.blocked = blocked;
		this.canMovementEnd = canMovementEnd;
		this.moveCost = moveCost;
	}

	public final String image;
	public final boolean blocked;
	public final boolean canMovementEnd;
	public final int moveCost;
}