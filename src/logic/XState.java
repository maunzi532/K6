package logic;

public enum XState
{
	PLAYERPHASE(2),
	ENTITY(1),
	HERO(1),
	BUILDING(0),
	TRANSPORTER(0),
	FLOOR(2);

	XState(int nextTarget)
	{
		this.nextTarget = nextTarget;
	}

	public final int nextTarget;
}