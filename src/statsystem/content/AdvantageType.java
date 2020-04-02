package statsystem.content;

public enum AdvantageType
{
	DEFEND("advtype.defend"),
	DAGGER("advtype.dagger"),
	SPEAR("advtype.spear"),
	AXE("advtype.axe"),
	SPELL("advtype.spell"),
	CROSSBOW("advtype.crossbow");

	public final CharSequence name;

	AdvantageType(CharSequence name)
	{
		this.name = name;
	}
}