package statsystem;

public enum AdaptiveType
{
	NONE("adaptive.none"),
	COST("adaptive.cost"),
	FINESSE("adaptive.finesse"),
	SKILL("adaptive.skill"),
	SPEED("adaptive.speed"),
	LUCK("adaptive.luck"),
	DEFENSE("adaptive.defense"),
	EVASION("adaptive.evasion");

	public final String name;

	AdaptiveType(String name)
	{
		this.name = name;
	}
}