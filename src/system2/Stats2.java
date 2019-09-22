package system2;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import entity.*;
import item.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import javafx.scene.control.*;
import javafx.stage.*;
import system2.content.*;

public class Stats2 implements Stats
{
	public static final int HEALTH_MULTIPLIER = 5;

	private XClass xClass;
	private int level;
	private PlayerLevelSystem playerLevelSystem;
	private String customName;
	private String customImage;
	private int strength;
	private int finesse;
	private int skill;
	private int speed;
	private int luck;
	private int defense;
	private int evasion;
	private int toughness;
	private int movement;
	private int currentHealth;
	private int exhaustion;
	private AttackMode2 lastUsed;
	private AttackItem2Slot slot;

	public Stats2(XClass xClass, int level, String customName,
			String customImage, int strength, int finesse, int skill, int speed,
			int luck, int defense, int evasion, int toughness,
			int movement, PlayerLevelSystem playerLevelSystem)
	{
		this.xClass = xClass;
		this.level = level;
		this.playerLevelSystem = playerLevelSystem;
		this.customName = customName;
		this.customImage = customImage;
		this.strength = strength;
		this.finesse = finesse;
		this.skill = skill;
		this.speed = speed;
		this.luck = luck;
		this.defense = defense;
		this.evasion = evasion;
		this.toughness = toughness;
		currentHealth = maxHealth();
		this.movement = movement;
		slot = new AttackItem2Slot(xClass.usableItems);
	}

	public Stats2(XClass xClass, int level, String customName,
			String customImage, int movement, PlayerLevelSystem playerLevelSystem)
	{
		this.xClass = xClass;
		this.level = level;
		this.playerLevelSystem = playerLevelSystem;
		autoStats();
		this.customName = customName;
		this.customImage = customImage;
		currentHealth = maxHealth();
		this.movement = movement;
		slot = new AttackItem2Slot(xClass.usableItems);
	}

	public Stats2(XClass xClass, int level, PlayerLevelSystem playerLevelSystem)
	{
		this.xClass = xClass;
		this.level = level;
		this.playerLevelSystem = playerLevelSystem;
		autoStats();
		slot = new AttackItem2Slot(xClass.usableItems);
	}

	public void autoStats()
	{
		strength = autoStat1(0);
		finesse = autoStat1(1);
		skill = autoStat1(2);
		speed = autoStat1(3);
		luck = autoStat1(4);
		defense = autoStat1(5);
		evasion = autoStat1(6);
		toughness = autoStat1(7);
		currentHealth = maxHealth();
		movement = xClass.movement;
	}

	private int autoStat1(int num)
	{
		if(playerLevelSystem != null)
			return playerLevelSystem.forLevel(num, level);
		else
			return xClass.getStat(num, level);
	}

	public XClass getxClass()
	{
		return xClass;
	}

	public int getLevel()
	{
		return level;
	}

	public int getStat1(int num)
	{
		return switch(num)
		{
			case 0 -> strength;
			case 1 -> finesse;
			case 2 -> skill;
			case 3 -> speed;
			case 4 -> luck;
			case 5 -> defense;
			case 6 -> evasion;
			case 7 -> toughness;
			default -> 0;
		};
	}

	public int getToughness()
	{
		return toughness;
	}

	public int getStrength()
	{
		return strength;
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

	public int getEvasion()
	{
		return evasion;
	}

	public int getCPower()
	{
		return toughness + strength + finesse + skill + speed + luck + defense + evasion;
	}

	public int getMovement()
	{
		return movement;
	}

	public int maxHealth()
	{
		return toughness * HEALTH_MULTIPLIER;
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
	public void autoEquip(InvEntity entity)
	{
		setLastUsed(((AttackItem2) entity.outputInv().viewRecipeItem(getItemFilter()).item).attackModes()
				.stream().findFirst().orElse(EmptyItem.INSTANCE.attackModes.get(0)));
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
		return maxHealth();
	}

	@Override
	public void change(int change)
	{
		currentHealth = Math.max(0, Math.min(maxHealth(), currentHealth + change));
	}

	@Override
	public int getRegenerateChange()
	{
		return maxHealth() - currentHealth;
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
	public String imagePath()
	{
		if(customImage != null)
			return customImage;
		else
			return "Enemy_0.png";
	}

	@Override
	public Stats copy()
	{
		Stats2 copy = new Stats2(xClass, level, customName, customImage, strength, finesse, skill, speed, luck, defense,
				evasion, toughness, movement, playerLevelSystem);
		copy.currentHealth = currentHealth;
		copy.exhaustion = exhaustion;
		return copy;
	}

	public Stats2(JrsObject data, ItemLoader itemLoader)
	{
		xClass = XClasses.INSTANCE.xClasses[((JrsNumber) data.get("Class")).getValue().intValue()];
		slot = new AttackItem2Slot(xClass.usableItems);
		level = ((JrsNumber) data.get("Level")).getValue().intValue();
		if(data.get("LevelSystem") != null)
		{
			playerLevelSystem = new PlayerLevelSystem(((JrsObject) data.get("LevelSystem")));
		}
		if(data.get("CustomName") != null)
		{
			customName = data.get("CustomName").asText();
		}
		if(data.get("CustomImage") != null)
		{
			customImage = data.get("CustomImage").asText();
		}
		strength = ((JrsNumber) data.get("Strength")).getValue().intValue();
		finesse = ((JrsNumber) data.get("Finesse")).getValue().intValue();
		skill = ((JrsNumber) data.get("Skill")).getValue().intValue();
		speed = ((JrsNumber) data.get("Speed")).getValue().intValue();
		luck = ((JrsNumber) data.get("Luck")).getValue().intValue();
		defense = ((JrsNumber) data.get("Defense")).getValue().intValue();
		evasion = ((JrsNumber) data.get("Evasion")).getValue().intValue();
		toughness = ((JrsNumber) data.get("Toughness")).getValue().intValue();
		movement = ((JrsNumber) data.get("Movement")).getValue().intValue();
		currentHealth = ((JrsNumber) data.get("CurrentHealth")).getValue().intValue();
		exhaustion = ((JrsNumber) data.get("Exhaustion")).getValue().intValue();
		if(data.get("LastUsed") != null)
		{
			lastUsed = ((AttackItem2) itemLoader.loadItem((JrsObject) data.get("LastUsedItem"))).attackModes()
					.stream().filter(e -> e.code == ((JrsNumber) data.get("LastUsed")).getValue().intValue()).findFirst().orElseThrow();
		}
	}

	@Override
	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		var a2 = a1.put("Class", xClass.code)
				.put("Level", level);
		if(playerLevelSystem != null)
		{
			a2 = playerLevelSystem.save(a2.startObjectField("LevelSystem")).end();
		}
		if(customName != null)
		{
			a2 = a2.put("CustomName", customName);
		}
		if(customImage != null)
		{
			a2 = a2.put("CustomImage", customImage);
		}
		var a3 = a2.put("Strength", strength)
				.put("Finesse", finesse)
				.put("Skill", skill)
				.put("Speed", speed)
				.put("Luck", luck)
				.put("Defense", defense)
				.put("Evasion", evasion)
				.put("Toughness", toughness)
				.put("Movement", movement)
				.put("CurrentHealth", currentHealth)
				.put("Exhaustion", exhaustion);
		if(lastUsed != null)
		{
			var a4 = a3.put("LastUsed", lastUsed.code).startObjectField("LastUsedItem");
			a3 = itemLoader.saveItem(a4, lastUsed.item).end();
		}
		return a3;
	}

	@Override
	public List<String> info()
	{
		List<String> info = new ArrayList<>();
		info.add("Class\n" + xClass.className);
		info.add("Level\n" + level);
		info.add("Health\n" + currentHealth + "/" + maxHealth());
		info.add("Strength\n" + strength);
		info.add("Finesse\n" + finesse);
		info.add("Skill\n" + skill);
		info.add("Speed\n" + speed);
		info.add("Luck\n" + luck);
		info.add("Defense\n" + defense);
		info.add("Evasion\n" + evasion);
		info.add("CPower\n" + getCPower());
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
	public String[] sideInfoText()
	{
		return new String[]{customName != null ? customName : "Enemy", xClass.className + " lv" + level};
	}

	@Override
	public List<String> infoEdit()
	{
		List<String> info = new ArrayList<>();
		info.add(customName != null ? "Name\n" + customName : "Generic\nName");
		info.add("Class\n" + xClass.className);
		info.add("Level\n" + level);
		info.add("Strength\n" + strength);
		info.add("Finesse\n" + finesse);
		info.add("Skill\n" + skill);
		info.add("Speed\n" + speed);
		info.add("Luck\n" + luck);
		info.add("Defense\n" + defense);
		info.add("Evasion\n" + evasion);
		info.add("Toughness\n" + toughness);
		info.add("Health\n" + currentHealth + "/" + maxHealth());
		info.add("Exhaustion\n" + exhaustion);
		info.add("Move\n" + movement);
		info.add("Defend\n" + (lastUsed != null ? lastUsed.item.info().get(0).replace("Type\n", "") : "None"));
		info.add("ItemTypes\n" + slot.getItemTypes().stream().map(e -> e.getSimpleName().replace("Item", ""))
				.collect(Collectors.joining("\n")));
		return info;
	}

	@Override
	public List<String> editOptions(int num)
	{
		if(num == 0)
			return List.of("Name", "Image");
		if(num == 1)
			return List.of("Prev", "Next");
		if(num == 2)
			return List.of("+", "-", "Reset\nstats");
		if(num <= 13)
			return List.of("+", "-", "Reset");
		if(num == 14)
			return List.of("Auto");
		return List.of();
	}

	@Override
	public void applyEditOption(int num, int option, XEntity entity)
	{
		switch((num << 4) + option)
		{
			case 0x00 ->
			{
				TextInputDialog td = new TextInputDialog(customName != null ? customName : "");
				td.setTitle("Edit name");
				td.setGraphic(null);
				td.setHeaderText(null);
				td.show();
				td.setOnHidden(e -> customName = td.getResult());
			}
			case 0x01 ->
			{
				File f = new FileChooser().showOpenDialog(null);
				if(f != null)
					customImage = f.getName();
				else
					customImage = null;
			}
			case 0x10 ->
			{
				xClass = XClasses.INSTANCE.xClasses[xClass.code - 1];
				slot = new AttackItem2Slot(xClass.usableItems);
			}
			case 0x11 ->
			{
				xClass = XClasses.INSTANCE.xClasses[xClass.code + 1];
				slot = new AttackItem2Slot(xClass.usableItems);
			}
			case 0x20 -> level++;
			case 0x21 -> level--;
			case 0x22 -> autoStats();
			case 0x30 -> strength++;
			case 0x31 -> strength--;
			case 0x32 -> strength = xClass.getStat(0, level);
			case 0x40 -> finesse++;
			case 0x41 -> finesse--;
			case 0x42 -> finesse = xClass.getStat(1, level);
			case 0x50 -> skill++;
			case 0x51 -> skill--;
			case 0x52 -> skill = xClass.getStat(2, level);
			case 0x60 -> speed++;
			case 0x61 -> speed--;
			case 0x62 -> speed = xClass.getStat(3, level);
			case 0x70 -> luck++;
			case 0x71 -> luck--;
			case 0x72 -> luck = xClass.getStat(4, level);
			case 0x80 -> defense++;
			case 0x81 -> defense--;
			case 0x82 -> defense = xClass.getStat(5, level);
			case 0x90 -> evasion++;
			case 0x91 -> evasion--;
			case 0x92 -> evasion = xClass.getStat(6, level);
			case 0xa0 -> toughness++;
			case 0xa1 -> toughness--;
			case 0xa2 -> toughness = xClass.getStat(7, level);
			case 0xb0 -> currentHealth++;
			case 0xb1 -> currentHealth--;
			case 0xb2 -> currentHealth = maxHealth();
			case 0xc0 -> exhaustion++;
			case 0xc1 -> exhaustion--;
			case 0xc2 -> exhaustion = 0;
			case 0xd0 -> movement++;
			case 0xd1 -> movement--;
			case 0xd2 -> movement = xClass.movement;
			case 0xe0 -> autoEquip((InvEntity) entity);
		}
	}
}