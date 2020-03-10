package system2;

public class AbilityText
{
	private final ModifierAspect modifierAspect;
	private final Ability2 ability;

	public AbilityText(ModifierAspect modifierAspect, Ability2 ability)
	{
		this.modifierAspect = modifierAspect;
		this.ability = ability;
	}

	public String text()
	{
		return "Ability\n" + ability.name + "\n" + modifierAspect.nameForAbility();
	}
}