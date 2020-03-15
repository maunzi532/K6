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
	public static final String[] statNames = new String[]
			{
					"Strength", "Finesse", "Skill", "Speed", "Luck", "Defense", "Evasion", "Toughness"
			};

	private XClass xClass;
	private int level;
	private int exp;
	private PlayerLevelSystem playerLevelSystem;
	private String customName;
	private String customImage;
	private int[] lvStats;
	private int movement;
	private int currentHealth;
	private int exhaustion;
	private AttackMode4 lastUsed;
	private AttackItem2Slot slot;

	public Stats(XClass xClass, int level, String customName,
			String customImage, int movement, PlayerLevelSystem playerLevelSystem)
	{
		this.xClass = xClass;
		this.level = level;
		this.playerLevelSystem = playerLevelSystem;
		lvStats = new int[8];
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

	public Stats(XClass xClass, int level, int exp, String customName, String customImage, int[] lvStats,
			int movement, int currentHealth, int exhaustion, PlayerLevelSystem playerLevelSystem)
	{
		this.xClass = xClass;
		this.level = level;
		this.exp = exp;
		this.playerLevelSystem = playerLevelSystem;
		this.customName = customName;
		this.customImage = customImage;
		this.lvStats = Arrays.copyOf(lvStats, 8);
		this.movement = movement;
		this.currentHealth = currentHealth;
		this.exhaustion = exhaustion;
		lastUsed = AttackMode4.EVADE_MODE;
		slot = new AttackItem2Slot(xClass.usableItems);
	}

	public Stats copy()
	{
		return new Stats(xClass, level, exp, customName, customImage, lvStats, movement, currentHealth, exhaustion, playerLevelSystem);
	}

	public void autoStats()
	{
		for(int i = 0; i < lvStats.length; i++)
		{
			if(playerLevelSystem != null)
				lvStats[i] = playerLevelSystem.forLevel(i, level);
			else
				lvStats[i] = xClass.getStat(i, level);
		}
		currentHealth = maxHealth();
		movement = xClass.movement;
	}

	public XClass xClass()
	{
		return xClass;
	}

	public int level()
	{
		return level;
	}

	public int exp()
	{
		return exp;
	}

	public void addExp(int amount)
	{
		if(level < levelSystem().levelCap())
			exp += amount;
	}

	public LevelSystem levelSystem()
	{
		return playerLevelSystem != null ? playerLevelSystem : xClass.levelSystem;
	}

	public int getStat1(int num)
	{
		return lvStats[num];
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

	public int toughness()
	{
		return lvStats[7];
	}

	public int strength()
	{
		return lvStats[0];
	}

	public int finesse()
	{
		return lvStats[1];
	}

	public int skill()
	{
		return lvStats[2];
	}

	public int speed()
	{
		return lvStats[3];
	}

	public int luck()
	{
		return lvStats[4];
	}

	public int defense()
	{
		return lvStats[5];
	}

	public int evasion()
	{
		return lvStats[6];
	}

	public int calcCPower()
	{
		return Arrays.stream(lvStats).sum();
	}

	public int movement()
	{
		return movement;
	}

	public int dashMovement()
	{
		return 12;
	}

	public int maxAccessRange()
	{
		return 4;
	}

	public int maxHealth()
	{
		return lvStats[7] * HEALTH_MULTIPLIER;
	}

	public int currentHealth()
	{
		return currentHealth;
	}

	public void setCurrentHealth(int currentHealth)
	{
		this.currentHealth = currentHealth;
	}

	public int exhaustion()
	{
		return exhaustion;
	}

	public AttackMode4 lastUsed()
	{
		return lastUsed;
	}

	public void equipMode(AttackMode4 mode)
	{
		lastUsed = mode;
	}

	public void autoEquip(XCharacter entity)
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

	public void afterTrading(XCharacter entity)
	{
		if(!lastUsed.active)
			return;
		if(!entity.outputInv().canGive(new ItemStack(lastUsed.item, 1), false))
		{
			lastUsed = AttackMode4.EVADE_MODE;
		}
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
		lvStats = new int[8];
		lvStats[0] = ((JrsNumber) data.get("Strength")).getValue().intValue();
		lvStats[1] = ((JrsNumber) data.get("Finesse")).getValue().intValue();
		lvStats[2] = ((JrsNumber) data.get("Skill")).getValue().intValue();
		lvStats[3] = ((JrsNumber) data.get("Speed")).getValue().intValue();
		lvStats[4] = ((JrsNumber) data.get("Luck")).getValue().intValue();
		lvStats[5] = ((JrsNumber) data.get("Defense")).getValue().intValue();
		lvStats[6] = ((JrsNumber) data.get("Evasion")).getValue().intValue();
		lvStats[7] = ((JrsNumber) data.get("Toughness")).getValue().intValue();
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
		var a3 = a2.put("Strength", lvStats[0])
				.put("Finesse", lvStats[1])
				.put("Skill", lvStats[2])
				.put("Speed", lvStats[3])
				.put("Luck", lvStats[4])
				.put("Defense", lvStats[5])
				.put("Evasion", lvStats[6])
				.put("Toughness", lvStats[7])
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
		for(int i = 0; i < 7; i++)
		{
			info.add(statNames[i] + "\n" + lvStats[i]);
		}
		info.add("CPower\n" + calcCPower());
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
		for(int i = 0; i < 8; i++)
		{
			info.add(statNames[i] + "\n" + lvStats[i]);
		}
		info.add("CPower\n" + calcCPower());
		return info;
	}

	public List<String> infoWithEquip()
	{
		return AttackMode3.convert(this, lastUsed).info();
	}

	public List<String> levelup()
	{
		int[] levelup = levelSystem().getLevelup(this);
		List<String> info = new ArrayList<>();
		info.add("Class\n" + xClass.className);
		info.add("Level\n" + level + " -> " + (level + 1));
		info.add("Exp\n" + exp + " -> " + ((exp - GetExpAnim.LEVELUP_EXP) / 2));
		for(int i = 0; i < 8; i++)
		{
			info.add(statNames[i] + "\n" + lvStats[i] + " -> " + (lvStats[i] + levelup[i]));
		}
		info.add("CPower\n" + calcCPower() + " -> " + (calcCPower() + Arrays.stream(levelup).sum()));
		int changedHealth = currentHealth;
		if(levelup[7] > 0)
			changedHealth += levelup[7] * HEALTH_MULTIPLIER;
		if(changedHealth > (lvStats[7] + levelup[7]) * HEALTH_MULTIPLIER)
			changedHealth = (lvStats[7] + levelup[7]) * HEALTH_MULTIPLIER;
		info.add("Health\n" + currentHealth + " -> " + changedHealth);
		info.add("Move\n" + movement);
		info.add(exhaustion > 0 ? "Exhausted\n" + exhaustion : "");
		level++;
		if(level < levelSystem().levelCap())
			exp = (exp - GetExpAnim.LEVELUP_EXP) / 2;
		else
			exp = 0;
		for(int i = 0; i < 8; i++)
		{
			lvStats[i] = lvStats[i] + levelup[i];
		}
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
		for(int i = 0; i < 8; i++)
		{
			info.add(statNames[i] + "\n" + lvStats[i]);
		}
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

	public void applyEditOption(int num, int option, XCharacter entity)
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
		int i = num - 4;
		if(i >= 0 && i < 8)
		{
			switch(option)
			{
				case 0 -> lvStats[i]++;
				case 1 -> lvStats[i]--;
				case 2 -> lvStats[i] = xClass.getStat(i, level);
			}
		}
	}
}