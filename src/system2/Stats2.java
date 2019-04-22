package system2;

import entity.*;

public class Stats2 implements Stats
{
	private int strength;
	private int finesse;
	private int skill;
	private int speed;
	private int luck;
	private int defense;
	private int toughness;
	private int endurance;
	private int movement;
	private int currentHealth;
	private int accumulatedDamage;
	private AttackMode2 lastUsed;

	public Stats2(int strength, int finesse, int skill, int speed, int luck, int defense, int toughness,
			int endurance, int movement)
	{
		this.strength = strength;
		this.finesse = finesse;
		this.skill = skill;
		this.speed = speed;
		this.luck = luck;
		this.defense = defense;
		this.toughness = toughness;
		this.endurance = endurance;
		this.movement = movement;
		currentHealth = toughness;
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

	public int getEndurance()
	{
		return endurance;
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

	public int getAccumulatedDamage()
	{
		return accumulatedDamage;
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
	public void change(boolean increase)
	{
		if(increase)
		{
			if(currentHealth < toughness)
				currentHealth++;
		}
		else
		{
			if(currentHealth > 0)
				currentHealth--;
			accumulatedDamage++;
		}
	}

	@Override
	public boolean removeEntity()
	{
		return currentHealth <= 0;
	}

	public static Stats2 create1()
	{
		return new Stats2(8, 5, 8, 7, 5, 9, 30, 30, 4);
	}

	public static Stats2 create2()
	{
		return new Stats2(6, 7, 10, 10, 2, 7, 25, 25, 4);
	}
}