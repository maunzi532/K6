package system2;

public enum AdaptiveType
{
	NONE("-"),
	COST("Health"),
	FINESSE("Finesse"),
	SKILL("Skill"),
	SPEED("Speed"),
	LUCK("Luck"),
	DEFENSE("Defense"),
	EVASION("Evasion");

	public String name;

	AdaptiveType(String name)
	{
		this.name = name;
	}
}