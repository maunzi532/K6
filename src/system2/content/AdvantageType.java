package system2.content;

public enum AdvantageType
{
	DEFEND("Defend"),
	DAGGER("Dagger"),
	SPEAR("Spear"),
	AXE("Axe"),
	SPELL("None"),
	CROSSBOW("None");

	public String name;

	AdvantageType(String name)
	{
		this.name = name;
	}
}