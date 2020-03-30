package statsystem;

public enum DefenseType
{
	PHYSICAL("Physical"),
	MAGICAL("Magical"),
	NONE("None");

	public final String text;

	DefenseType(String text)
	{
		this.text = text;
	}

	public static DefenseType inverted(DefenseType defenseType)
	{
		return switch(defenseType)
				{
					case PHYSICAL -> MAGICAL;
					case MAGICAL -> PHYSICAL;
					case NONE -> NONE;
				};
	}
}