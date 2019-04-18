package system1;

import entity.*;

public class Stats1 implements Stats
{
	private int maxHealth;
	private int attack;
	private int accuracy;
	private int speed;
	private int crit;
	private int defense;
	private int movement;
	private int currentHealth;
	private boolean moved;
	private int ap;
	private boolean finished;

	public Stats1(int maxHealth, int attack, int accuracy, int speed, int crit, int defense, int movement)
	{
		this.maxHealth = maxHealth;
		this.attack = attack;
		this.accuracy = accuracy;
		this.speed = speed;
		this.crit = crit;
		this.defense = defense;
		this.movement = movement;
		currentHealth = maxHealth;
	}

	public void setCurrentHealth(int currentHealth)
	{
		this.currentHealth = currentHealth;
	}

	public int getMaxHealth()
	{
		return maxHealth;
	}

	public int getAttack()
	{
		return attack;
	}

	public int getAccuracy()
	{
		return accuracy;
	}

	public int getSpeed()
	{
		return speed;
	}

	public int getCrit()
	{
		return crit;
	}

	public int getDefense()
	{
		return defense;
	}

	public int getMovement()
	{
		return movement;
	}

	public int getCurrentHealth()
	{
		return currentHealth;
	}

	@Override
	public int getStat(int num)
	{
		return currentHealth;
	}

	@Override
	public int getMaxStat(int num)
	{
		return maxHealth;
	}

	@Override
	public void change(boolean increase)
	{
		if(increase)
		{
			if(currentHealth < maxHealth)
				currentHealth++;
		}
		else
		{
			if(currentHealth > 0)
				currentHealth--;
		}
	}

	@Override
	public boolean removeEntity()
	{
		return currentHealth <= 0;
	}

	public static Stats1 create1()
	{
		return new Stats1(20, 10, 10, 5, 5, 5, 4);
	}

	public static Stats1 create2()
	{
		return new Stats1(20, 7, 10, 10, 2, 4, 4);
	}
}