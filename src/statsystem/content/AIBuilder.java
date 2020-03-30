package statsystem.content;

import java.util.*;
import statsystem.*;

public final class AIBuilder
{
	private AI2Class itemClass;
	private boolean autoImage;
	private String image;
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
	private boolean autoDefenseType;
	private DefenseType defenseType;
	private boolean autoRanges;
	private int[] ranges;
	private int[] counterR;
	private boolean autoAbilities;
	private List<Ability2> abilities;
	private boolean autoModes;
	private List<AM2Type> attackModes;

	public AIBuilder(AI2Class itemClass, int damage, int heavy, int accuracy)
	{
		this.itemClass = itemClass;
		this.damage = damage;
		this.heavy = heavy;
		this.accuracy = accuracy;
		autoImage = true;
		autoAdaptive = true;
		autoAdv = true;
		autoDefenseType = true;
		autoRanges = true;
		autoAbilities = true;
		autoModes = true;
		abilities = new ArrayList<>();
		attackModes = new ArrayList<>();
	}

	public AIBuilder setCrit(int crit)
	{
		this.crit = crit;
		return this;
	}

	public AIBuilder setSlow(int slow)
	{
		this.slow = slow;
		return this;
	}

	public AIBuilder setImage(String image)
	{
		autoImage = false;
		this.image = image;
		return this;
	}

	public AIBuilder setAdaptive(int adaptive, AdaptiveType adaptiveType)
	{
		autoAdaptive = false;
		this.adaptive = adaptive;
		this.adaptiveType = adaptiveType;
		return this;
	}

	public AIBuilder setAdvantageType(AdvantageType advantageType)
	{
		autoAdv = false;
		this.advantageType = advantageType;
		return this;
	}

	public AIBuilder setDefenseType(DefenseType defenseType)
	{
		autoDefenseType = false;
		this.defenseType = defenseType;
		return this;
	}

	public AIBuilder setRanges(int[] ranges)
	{
		autoRanges = false;
		this.ranges = ranges;
		counterR = ranges;
		return this;
	}

	public AIBuilder setRanges(int[] ranges, int[] counterR)
	{
		autoRanges = false;
		this.ranges = ranges;
		this.counterR = counterR;
		return this;
	}

	public AIBuilder replaceAbilities()
	{
		autoAbilities = false;
		return this;
	}

	public AIBuilder addAbility(Ability2 ability)
	{
		abilities.add(ability);
		return this;
	}

	public AIBuilder replaceModes()
	{
		autoModes = false;
		return this;
	}

	public AIBuilder addMode(AM2Type mode)
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
				autoImage ? itemClass.image() : image,
				damage, heavy, accuracy, crit, slow,
				autoAdaptive ? itemClass.adaptive() : adaptive,
				autoAdaptive ? itemClass.adaptiveType() : adaptiveType,
				autoAdv ? itemClass.advType() : advantageType,
				autoDefenseType ? itemClass.defenseType() : defenseType,
				autoRanges ? itemClass.ranges() : ranges,
				autoRanges ? itemClass.counterR() : counterR,
				abilities, attackModes);
	}
}