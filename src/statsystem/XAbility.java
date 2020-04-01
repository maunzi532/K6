package statsystem;

public enum XAbility
{
	TWO_HANDED("ability.twohanded"),
	FAST("ability.fast"),
	MELTING("ability.melting"),
	DEFENDER("ability.defender"),
	UNLIMITED_CRITICAL("ability.unlimitedcritical"),
	SHIELD_POWER("ability.shieldpower"),
	TEAMSTRIKE("ability.teamstrike"),
	LIFE_REFUND("ability.liferefund"),
	CRITICAL_TARGET("ability.criticaltarget"),
	FAST_FIRE("ability.fastfire");

	public final String name;

	XAbility(String name)
	{
		this.name = name;
	}
}