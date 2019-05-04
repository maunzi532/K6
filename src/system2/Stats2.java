package system2;

import entity.*;
import item.*;
import java.util.*;
import javafx.scene.image.*;

public class Stats2 implements Stats
{
	private XClass xClass;
	private int level;
	private String customName;
	private int strength;
	private int finesse;
	private int skill;
	private int speed;
	private int luck;
	private int defense;
	private int magicDef;
	private int toughness;
	private int movement;
	private int currentHealth;
	private int exhaustion;
	private AttackMode2 lastUsed;
	private AttackItem2Slot slot;

	public Stats2(XClass xClass, int level, String customName, int strength, int finesse, int skill, int speed, int luck, int defense, int magicDef, int toughness,
			int movement)
	{
		this.xClass = xClass;
		this.level = level;
		this.customName = customName;
		this.strength = strength;
		this.finesse = finesse;
		this.skill = skill;
		this.speed = speed;
		this.luck = luck;
		this.defense = defense;
		this.magicDef = magicDef;
		this.toughness = toughness;
		currentHealth = toughness;
		this.movement = movement;
		slot = new AttackItem2Slot(xClass.usableItems);
	}

	public Stats2(XClass xClass, int level)
	{
		this.xClass = xClass;
		this.level = level;
		strength = xClass.getStat(0, level);
		finesse = xClass.getStat(1, level);
		skill = xClass.getStat(2, level);
		speed = xClass.getStat(3, level);
		luck = xClass.getStat(4, level);
		defense = xClass.getStat(5, level);
		magicDef = xClass.getStat(6, level);
		toughness = xClass.getStat(7, level);
		currentHealth = toughness;
		movement = xClass.movement;
		slot = new AttackItem2Slot(xClass.usableItems);
	}

	public XClass getxClass()
	{
		return xClass;
	}

	public int getLevel()
	{
		return level;
	}

	public int getStrength()
	{
		return strength;
	}

	public int getToughness()
	{
		return toughness;
	}

	public int getFinesse()
	{
		return finesse;
	}

	public int getSkill()
	{
		return skill;
	}

	public int getSpeed()
	{
		return speed;
	}

	public int getLuck()
	{
		return luck;
	}

	public int getDefense()
	{
		return defense;
	}

	public int getMagicDef()
	{
		return magicDef;
	}

	public int getMovement()
	{
		return movement;
	}

	public int getCurrentHealth()
	{
		return currentHealth;
	}

	public void setCurrentHealth(int currentHealth)
	{
		this.currentHealth = currentHealth;
	}

	public int getExhaustion()
	{
		return exhaustion;
	}

	public AttackMode2 getLastUsed()
	{
		return lastUsed;
	}

	public void setLastUsed(AttackMode2 lastUsed)
	{
		this.lastUsed = lastUsed;
	}

	@Override
	public Item getItemFilter()
	{
		return slot;
	}

	@Override
	public int getStat(int num)
	{
		return currentHealth;
	}

	@Override
	public int getMaxStat(int num)
	{
		return toughness;
	}

	@Override
	public void change(int change)
	{
		currentHealth = Math.max(0, Math.min(toughness, currentHealth + change));
	}

	@Override
	public int getRegenerateChange()
	{
		return toughness - currentHealth;
	}

	@Override
	public void regenerating()
	{
		exhaustion++;
	}

	@Override
	public boolean removeEntity()
	{
		return currentHealth <= 0;
	}

	@Override
	public String getName()
	{
		return customName != null ? customName : xClass.className + " lv" + level;
	}

	@Override
	public List<String> info()
	{
		List<String> info = new ArrayList<>();
		info.add("Class\n" + xClass.className);
		info.add("Level\n" + level);
		info.add("Health\n" + currentHealth + "/" + toughness);
		info.add("Strength\n" + strength);
		info.add("Finesse\n" + finesse);
		info.add("Skill\n" + skill);
		info.add("Speed\n" + speed);
		info.add("Luck\n" + luck);
		info.add("Defense\n" + defense + " / " + magicDef);
		info.add("Move\n" + movement);
		info.add(exhaustion > 0 ? "Exhausted\n" + exhaustion : "");
		info.add("Defend\n" + (lastUsed != null ? lastUsed.item.info().get(0).replace("Type\n", "") : "None"));
		for(Class e : slot.getItemTypes())
		{
			info.add("ItemType\n" + e.getSimpleName().replace("Item", ""));
		}
		for(Ability2 ability : xClass.abilities)
		{
			info.add("Ability\n" + ability.name);
		}
		return info;
	}

	@Override
	public Image image()
	{
		return new Image("Blue2.png");
	}
}