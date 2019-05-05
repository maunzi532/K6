package system2;

import entity.*;
import item.*;
import java.nio.*;
import java.util.*;
import javafx.scene.image.*;
import system2.content.*;

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
	public Image image()
	{
		return new Image("TA_3.png");
	}

	@Override
	public List<Integer> save()
	{
		List<Integer> ints = new ArrayList<>();
		ints.add(xClass.code);
		ints.add(level);
		if(customName != null)
		{
			char[] customNameChars = customName.toCharArray();
			ints.add(customNameChars.length);
			for(int i = 0; i < customNameChars.length; i++)
			{
				ints.add((int) customNameChars[i]);
			}
		}
		else
		{
			ints.add(-1);
		}
		ints.add(strength);
		ints.add(finesse);
		ints.add(skill);
		ints.add(speed);
		ints.add(luck);
		ints.add(defense);
		ints.add(magicDef);
		ints.add(toughness);
		ints.add(movement);
		ints.add(currentHealth);
		ints.add(exhaustion);
		if(lastUsed != null)
		{
			ints.add(lastUsed.code);
			ints.addAll(lastUsed.item.save());
		}
		else
		{
			ints.add(-1);
		}
		return ints;
	}

	public Stats2(IntBuffer intBuffer, CombatSystem s1)
	{
		xClass = XClasses.INSTANCE.xClasses[intBuffer.get()];
		slot = new AttackItem2Slot(xClass.usableItems);
		level = intBuffer.get();
		int cncl = intBuffer.get();
		if(cncl >= 0)
		{
			char[] customNameChars = new char[cncl];
			for(int i = 0; i < customNameChars.length; i++)
			{
				customNameChars[i] = (char) intBuffer.get();
			}
			customName = new String(customNameChars);
		}
		strength = intBuffer.get();
		finesse = intBuffer.get();
		skill = intBuffer.get();
		speed = intBuffer.get();
		luck = intBuffer.get();
		defense = intBuffer.get();
		magicDef = intBuffer.get();
		toughness = intBuffer.get();
		movement = intBuffer.get();
		currentHealth = intBuffer.get();
		exhaustion = intBuffer.get();
		int clu = intBuffer.get();
		if(clu >= 0)
		{
			lastUsed = ((AttackItem2) s1.loadItem(intBuffer)).attackModes().stream().filter(e -> e.code == clu).findFirst().orElseThrow();
		}
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
}