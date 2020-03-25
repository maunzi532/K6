package entity;

public enum CharacterTeam
{
	HERO("arrow.healthbar.hero"),
	ENEMY("arrow.healthbar.enemy");

	public final String healthBarColor;

	CharacterTeam(String healthBarColor)
	{
		this.healthBarColor = healthBarColor;
	}
}