package system2;

import entity.*;

public class Stats2 implements Stats
{
	private XClass xClass;
	private int level;
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

	public Stats2(XClass xClass, int level, int strength, int finesse, int skill, int speed, int luck, int defense, int magicDef, int toughness,
			int movement)
	{
		this.xClass = xClass;
		this.level = level;
		this.strength = strength;
		this.finesse = finesse;
		this.skill = skill;
		this.speed = speed;
		this.luck = luck;
		this.defense = defense;
		this.magicDef = magicDef;
		this.toughness = toughness;
		this.movement = movement;
		currentHealth = toughness;
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

	/*public static Stats2 create1()
	{
		return new Stats2(8, 5, 8, 7, 5, 9, 44, 30, 30, 4);
	}

	public static Stats2 create2()
	{
		return new Stats2(6, 7, 10, 10, 2, 7, 4, 25, 25, 4);
	}*/
}