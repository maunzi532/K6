package statsystem;

public enum DefenseType
{
	PHYSICAL("attackitem.defensetype.physical"),
	MAGICAL("attackitem.defensetype.magical"),
	NONE("attackitem.defensetype.none");

	public final CharSequence text;

	DefenseType(CharSequence text)
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