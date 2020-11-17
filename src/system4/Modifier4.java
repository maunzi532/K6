package system4;

public class Modifier4
{
	private String stat;
	private ModifierType4 type;
	private int amount;

	public Modifier4(String stat, ModifierType4 type, int amount)
	{
		this.stat = stat;
		this.type = type;
		this.amount = amount;
	}

	public String stat()
	{
		return stat;
	}

	public ModifierType4 type()
	{
		return type;
	}

	public int amount()
	{
		return amount;
	}

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