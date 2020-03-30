package statsystem;

public enum AttackSide
{
	INITIATOR,
	TARGET;

	public static AttackSide inverted(AttackSide side)
	{
		return switch(side)
				{
					case INITIATOR -> TARGET;
					case TARGET -> INITIATOR;
				};
	}
}