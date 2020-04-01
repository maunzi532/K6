package statsystem;

public final class AbilityText
{
	private final ModifierAspect modifierAspect;
	private final XAbility ability;

	public AbilityText(ModifierAspect modifierAspect, XAbility ability)
	{
		this.modifierAspect = modifierAspect;
		this.ability = ability;
	}

	public String text()
	{
		return "Ability\n" + ability.name + "\n" + modifierAspect.nameForAbility();
	}
}