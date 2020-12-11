package levelmap;

public enum FloorTileType
{
	BLUE("floortile.blue", "floortile.blue", false, true, 1),
	GSL("floortile.gsl", "floortile.gsl", false, true, 2),
	TECH("floortile.tech", "floortile.tech", false, true, 1),
	WALL("floortile.wall", "floortile.wall", true, false, 1),
	WOOD("floortile.wood", "floortile.wood", false, true, 1);

	//Hex height same as Quad height

	public final String text;
	public final String image;
	public final boolean blocked;
	public final boolean canMovementEnd;
	public final int moveCost;

	FloorTileType(String text, String image, boolean blocked, boolean canMovementEnd, int moveCost)
	{
		this.text = text;
		this.image = image;
		this.blocked = blocked;
		this.canMovementEnd = canMovementEnd;
		this.moveCost = moveCost;
	}
}