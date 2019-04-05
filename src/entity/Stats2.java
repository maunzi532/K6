package entity;

public class Stats2 implements Stats1
{
	private int maxHealth;
	private int attack;
	private int accuracy;
	private int speed;
	private int crit;
	private int defense;
	private int movement;
	private int currentHealth;

	public Stats2(int maxHealth, int attack, int accuracy, int speed, int crit, int defense, int movement)
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

	public static Stats2 create1()
	{
		return new Stats2(20, 10, 10, 5, 5, 5, 4);
	}

	public static Stats2 create2()
	{
		return new Stats2(20, 7, 10, 10, 2, 4, 4);
	}
}