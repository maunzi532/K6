package entity;

public enum CharacterTeam
{
	HERO("sideinfo.generic.hero", "arrow.healthbar.hero"),
	ENEMY("sideinfo.generic.enemy", "arrow.healthbar.enemy");

	public final String genericName;
	public final String healthBarColor;

	CharacterTeam(String genericName, String healthBarColor)
	{
		this.genericName = genericName;
		this.healthBarColor = healthBarColor;
	}
}