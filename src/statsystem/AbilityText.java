package statsystem;

import text.*;

public final class AbilityText
{
	private final ModifierAspect modifierAspect;
	private final XAbility ability;

	public AbilityText(ModifierAspect modifierAspect, XAbility ability)
	{
		this.modifierAspect = modifierAspect;
		this.ability = ability;
	}

	public CharSequence text()
	{
		return MultiText.lines(ability.name, new ArgsText("ability.withmode", new ArgsText(modifierAspect.nameForAbility().toString())));
	}
}