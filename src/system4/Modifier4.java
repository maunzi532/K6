package system4;

public record Modifier4(Stats4 stat, ModifierType4 type, int amount)
{
	public int apply(int value)
	{
		return switch(type)
		{
			case ADD -> value + amount;
			case MULT -> value * amount;
			case DIV -> Math.floorDiv(value, amount);
			case SET -> amount;
		};
	}
}