package system2.content;

import java.util.*;
import system2.*;

public class AI2Builder
{
	private AI2Class itemClass;
	private boolean autoImage;
	private String imageName;
	private int damage;
	private int heavy;
	private int accuracy;
	private int crit;
	private int slow;
	private boolean autoAdaptive;
	private int adaptive;
	private AdaptiveType adaptiveType;
	private boolean autoAdv;
	private AdvantageType advantageType;
	private boolean autoMagical;
	private boolean magical;
	private boolean autoRanges;
	private int[] ranges;
	private int[] counterR;
	private boolean autoAbilities;
	private List<Ability2> abilities;
	private boolean autoModes;
	private List<AM2Type> attackModes;

	public AI2Builder(AI2Class itemClass, int damage, int heavy, int accuracy)
	{
		this.itemClass = itemClass;
		this.damage = damage;
		this.heavy = heavy;
		this.accuracy = accuracy;
		autoImage = true;
		autoAdaptive = true;
		autoAdv = true;
		autoMagical = true;
		autoRanges = true;
		autoAbilities = true;
		autoModes = true;
		abilities = new ArrayList<>();
		attackModes = new ArrayList<>();
	}

	public AI2Builder crit(int crit)
	{
		this.crit = crit;
		return this;
	}

	public AI2Builder slow(int slow)
	{
		this.slow = slow;
		return this;
	}

	public AI2Builder image(String imageName)
	{
		autoImage = false;
		this.imageName = imageName;
		return this;
	}

	public AI2Builder adaptive(int adaptive, AdaptiveType adaptiveType)
	{
		autoAdaptive = false;
		this.adaptive = adaptive;
		this.adaptiveType = adaptiveType;
		return this;
	}

	public AI2Builder advantageType(AdvantageType advantageType)
	{
		autoAdv = false;
		this.advantageType = advantageType;
		return this;
	}

	public AI2Builder isMagical(boolean magical)
	{
		autoMagical = false;
		this.magical = magical;
		return this;
	}

	public AI2Builder ranges(int... ranges)
	{
		autoRanges = false;
		this.ranges = ranges;
		counterR = ranges;
		return this;
	}

	public AI2Builder ranges(int[] ranges, int[] counterR)
	{
		autoRanges = false;
		this.ranges = ranges;
		this.counterR = counterR;
		return this;
	}

	public AI2Builder replaceAbilities()
	{
		autoAbilities = false;
		return this;
	}

	public AI2Builder addAbility(Ability2 ability)
	{
		abilities.add(ability);
		return this;
	}

	public AI2Builder replaceModes()
	{
		autoModes = false;
		return this;
	}

	public AI2Builder addMode(AM2Type mode)
	{
		attackModes.add(mode);
		return this;
	}

	public AttackItem build()
	{
		if(autoAbilities)
			abilities.addAll(itemClass.abilities());
		if(autoModes)
			attackModes.addAll(itemClass.attackModes());
		return new AttackItem(itemClass,
				autoImage ? itemClass.imageName() : imageName,
				damage, heavy, accuracy, crit, slow,
				autoAdaptive ? itemClass.adaptive() : adaptive,
				autoAdaptive ? itemClass.adaptiveType() : adaptiveType,
				autoAdv ? itemClass.advType() : advantageType,
				autoMagical ? itemClass.magical() : magical,
				autoRanges ? itemClass.ranges() : ranges,
				autoRanges ? itemClass.counterR() : counterR,
				abilities, attackModes);
	}
}