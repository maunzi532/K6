package system2;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import entity.*;
import item.*;
import item.view.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import javafx.scene.control.*;
import javafx.stage.*;
import system2.animation.*;
import system2.content.*;

public class Stats implements ModifierAspect
{
	public static final int HEALTH_MULTIPLIER = 5;

	private XClass xClass;
	private int level;
	private int exp;
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
	private AttackMode4 lastUsed;
	private AttackItem2Slot slot;

	public Stats(XClass xClass, int level, int exp, String customName,
			String customImage, int strength, int finesse, int skill, int speed,
			int luck, int defense, int evasion, int toughness,
			int movement, PlayerLevelSystem playerLevelSystem)
	{
		this.xClass = xClass;
		this.level = level;
		this.exp = exp;
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
		lastUsed = AttackMode4.EVADE_MODE;
		slot = new AttackItem2Slot(xClass.usableItems);
	}

	public Stats(XClass xClass, int level, String customName,
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
		lastUsed = AttackMode4.EVADE_MODE;
		slot = new AttackItem2Slot(xClass.usableItems);
	}

	public Stats(XClass xClass, int level, PlayerLevelSystem playerLevelSystem)
	{
		this.xClass = xClass;
		this.level = level;
		this.playerLevelSystem = playerLevelSystem;
		autoStats();
		lastUsed = AttackMode4.EVADE_MODE;
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

	public int getExp()
	{
		return exp;
	}

	public void addExp(int amount)
	{
		if(level < getLevelSystem().levelCap())
			exp += amount;
	}

	public LevelSystem getLevelSystem()
	{
		return playerLevelSystem != null ? playerLevelSystem : xClass.levelSystem;
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

	@Override
	public String nameForAbility()
	{
		return customName != null ? customName : "Char";
	}

	@Override
	public List<Ability2> abilities()
	{
		return List.of();
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

	public AttackMode4 getLastUsed()
	{
		return lastUsed;
	}

	public void setLastUsed(AttackMode4 lastUsed)
	{
		this.lastUsed = lastUsed;
	}

	public void autoEquip(XEntity entity)
	{
		ItemView itemView = entity.outputInv().viewRecipeItem(getItemFilter());
		if(itemView.base == 0 || !(itemView.item instanceof AttackItem2))
		{
			lastUsed = AttackMode4.EVADE_MODE;
		}
		else
		{
			lastUsed = ((AttackItem2) itemView.item).attackModes()
					.stream().findFirst().orElse(AttackMode4.EVADE_MODE);
		}
	}

	public void afterTrading(XEntity entity)
	{
		if(!lastUsed.active)
			return;
		if(!entity.outputInv().canGive(new ItemStack(lastUsed.item, 1), false))
		{
			lastUsed = AttackMode4.EVADE_MODE;
		}
	}

	public void equip(AttackMode4 mode)
	{
		if(mode != null)
		{
			lastUsed = mode;
		}
		else
		{
			lastUsed = AttackMode4.EVADE_MODE;
		}
	}

	public AttackMode4 getEquippedMode()
	{
		if(!lastUsed.active)
			return null;
		else
			return lastUsed;
	}

	public Item getItemFilter()
	{
		return slot;
	}

	public int getVisualStat(int num)
	{
		return switch(num)
				{
					case 0 -> currentHealth;
					case 1 -> exp;
					default -> throw new IllegalStateException("Unexpected value: " + num);
				};
	}

	public int getMaxVisualStat(int num)
	{
		return switch(num)
				{
					case 0 -> maxHealth();
					case 1 -> 100;
					default -> throw new IllegalStateException("Unexpected value: " + num);
				};
	}

	public int getRegenerateChange()
	{
		return maxHealth() - currentHealth;
	}

	public void regenerating()
	{
		exhaustion++;
	}

	public String getName()
	{
		return customName != null ? customName : xClass.className + " lv" + level;
	}

	public String imagePath()
	{
		if(customImage != null)
			return customImage;
		else
			return "Enemy_0.png";
	}

	public Stats copy()
	{
		Stats copy = new Stats(xClass, level, exp, customName, customImage, strength, finesse, skill, speed, luck, defense,
				evasion, toughness, movement, playerLevelSystem);
		copy.currentHealth = currentHealth;
		copy.exhaustion = exhaustion;
		return copy;
	}

	public Stats(JrsObject data, ItemLoader itemLoader)
	{
		xClass = XClasses.INSTANCE.xClasses[((JrsNumber) data.get("Class")).getValue().intValue()];
		slot = new AttackItem2Slot(xClass.usableItems);
		level = ((JrsNumber) data.get("Level")).getValue().intValue();
		exp = ((JrsNumber) data.get("Exp")).getValue().intValue();
		exp = 100; //TODO
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
			AttackItem2 item1 = ((AttackItem2) itemLoader.loadItem((JrsObject) data.get("LastUsedItem")));
			lastUsed = item1.attackModes().stream().filter(e -> e.mode.code() == ((JrsNumber) data.get("LastUsed")).getValue().intValue())
					.findFirst().orElseThrow();
		}
		else
		{
			lastUsed = AttackMode4.EVADE_MODE;
		}
	}

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		var a2 = a1.put("Class", xClass.code)
				.put("Level", level)
				.put("Exp", exp);
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
		if(lastUsed.active)
		{
			var a4 = a3.put("LastUsed", lastUsed.mode.code()).startObjectField("LastUsedItem");
			a3 = itemLoader.saveItem(a4, lastUsed.item).end();
		}
		return a3;
	}

	public List<String> info()
	{
		List<String> info = new ArrayList<>();
		info.add("Class\n" + xClass.className);
		info.add("Level\n" + level);
		info.add("Exp\n" + exp);
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
		info.add("Defend\n" + (lastUsed.active ? lastUsed.item.info().get(0).replace("Type\n", "") : "None"));
		for(AI2Class e : slot.getItemTypes())
		{
			info.add("ItemType\n" + e.getClass().getSimpleName().replace("Item", ""));
		}
		for(Ability2 ability : xClass.abilities)
		{
			info.add("Ability\n" + ability.name);
		}
		return info;
	}

	public List<String> statsInfo()
	{
		List<String> info = new ArrayList<>();
		info.add("Class\n" + xClass.className);
		info.add("Level\n" + level);
		info.add("Exp\n" + exp);
		info.add("Health\n" + currentHealth + "/" + maxHealth());
		info.add("Move\n" + movement);
		info.add(exhaustion > 0 ? "Exhausted\n" + exhaustion : exhaustion < 0 ? "Boosted\n" + (-exhaustion) : "");
		info.add("Strength\n" + strength);
		info.add("Finesse\n" + finesse);
		info.add("Skill\n" + skill);
		info.add("Speed\n" + speed);
		info.add("Luck\n" + luck);
		info.add("Defense\n" + defense);
		info.add("Evasion\n" + evasion);
		info.add("Toughness\n" + toughness);
		info.add("CPower\n" + getCPower());
		return info;
	}

	public List<String> infoWithEquip()
	{
		return AttackMode3.convert(this, lastUsed).info();
	}

	public List<String> levelup()
	{
		int[] levelup = getLevelSystem().getLevelup(this);
		List<String> info = new ArrayList<>();
		info.add("Class\n" + xClass.className);
		info.add("Level\n" + level + " -> " + (level + 1));
		info.add("Exp\n" + exp + " -> " + ((exp - GetExpAnim.LEVELUP_EXP) / 2));
		info.add("Strength\n" + strength + " -> " + (strength + levelup[0]));
		info.add("Finesse\n" + finesse + " -> " + (finesse + levelup[1]));
		info.add("Skill\n" + skill + " -> " + (skill + levelup[2]));
		info.add("Speed\n" + speed + " -> " + (speed + levelup[3]));
		info.add("Luck\n" + luck + " -> " + (luck + levelup[4]));
		info.add("Defense\n" + defense + " -> " + (defense + levelup[5]));
		info.add("Evasion\n" + evasion + " -> " + (evasion + levelup[6]));
		info.add("Toughness\n" + toughness + " -> " + (toughness + levelup[7]));
		info.add("CPower\n" + getCPower() + " -> " + (getCPower() + Arrays.stream(levelup).sum()));
		int changedHealth = currentHealth;
		if(levelup[7] > 0)
			changedHealth += levelup[7] * HEALTH_MULTIPLIER;
		if(changedHealth > (toughness + levelup[7]) * HEALTH_MULTIPLIER)
			changedHealth = (toughness + levelup[7]) * HEALTH_MULTIPLIER;
		info.add("Health\n" + currentHealth + " -> " + changedHealth);
		info.add("Move\n" + movement);
		info.add(exhaustion > 0 ? "Exhausted\n" + exhaustion : "");
		level++;
		if(level < getLevelSystem().levelCap())
			exp = (exp - GetExpAnim.LEVELUP_EXP) / 2;
		else
			exp = 0;
		strength = strength + levelup[0];
		finesse = finesse + levelup[1];
		skill = skill + levelup[2];
		speed = speed + levelup[3];
		luck = luck + levelup[4];
		defense = defense + levelup[5];
		evasion = evasion + levelup[6];
		toughness = toughness + levelup[7];
		currentHealth = changedHealth;
		return info;
	}

	public String[] sideInfoText()
	{
		return new String[]{customName != null ? customName : "Enemy", xClass.className + " lv" + level};
	}

	public List<String> infoEdit()
	{
		List<String> info = new ArrayList<>();
		info.add(customName != null ? "Name\n" + customName : "Generic\nName");
		info.add("Class\n" + xClass.className);
		info.add("Level\n" + level);
		info.add("Exp\n" + exp);
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
		info.add("Defend\n" + (lastUsed.active ? lastUsed.item.info().get(0).replace("Type\n", "") : "None"));
		info.add("ItemTypes\n" + slot.getItemTypes().stream().map(e -> e.getClass().getSimpleName().replace("Item", ""))
				.collect(Collectors.joining("\n")));
		return info;
	}

	public List<String> editOptions(int num)
	{
		if(num == 0)
			return List.of("Name", "Image");
		if(num == 1)
			return List.of("Prev", "Next");
		if(num == 2)
			return List.of("+", "-", "Reset\nstats");
		if(num <= 14)
			return List.of("+", "-", "Reset");
		if(num == 15)
			return List.of("Auto");
		return List.of();
	}

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
			case 0x30 -> exp++;
			case 0x31 -> exp--;
			case 0x32 -> exp = 0;
			case 0x40 -> strength++;
			case 0x41 -> strength--;
			case 0x42 -> strength = xClass.getStat(0, level);
			case 0x50 -> finesse++;
			case 0x51 -> finesse--;
			case 0x52 -> finesse = xClass.getStat(1, level);
			case 0x60 -> skill++;
			case 0x61 -> skill--;
			case 0x62 -> skill = xClass.getStat(2, level);
			case 0x70 -> speed++;
			case 0x71 -> speed--;
			case 0x72 -> speed = xClass.getStat(3, level);
			case 0x80 -> luck++;
			case 0x81 -> luck--;
			case 0x82 -> luck = xClass.getStat(4, level);
			case 0x90 -> defense++;
			case 0x91 -> defense--;
			case 0x92 -> defense = xClass.getStat(5, level);
			case 0xa0 -> evasion++;
			case 0xa1 -> evasion--;
			case 0xa2 -> evasion = xClass.getStat(6, level);
			case 0xb0 -> toughness++;
			case 0xb1 -> toughness--;
			case 0xb2 -> toughness = xClass.getStat(7, level);
			case 0xc0 -> currentHealth++;
			case 0xc1 -> currentHealth--;
			case 0xc2 -> currentHealth = maxHealth();
			case 0xd0 -> exhaustion++;
			case 0xd1 -> exhaustion--;
			case 0xd2 -> exhaustion = 0;
			case 0xe0 -> movement++;
			case 0xe1 -> movement--;
			case 0xe2 -> movement = xClass.movement;
			case 0xf0 -> autoEquip(entity);
		}
	}
}