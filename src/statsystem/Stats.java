package statsystem;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import entity.*;
import item.*;
import item.view.*;
import java.io.*;
import java.util.*;
import javafx.scene.control.*;
import javafx.stage.*;
import statsystem.animation.*;
import statsystem.content.*;
import text.*;

public final class Stats implements ModifierAspect
{
	private static final int HEALTH_MULTIPLIER = 5;
	private static final String[] statNames =
			{
					"strength", "finesse", "skill", "speed", "luck", "defense", "evasion", "toughness"
			};

	private XClass xClass;
	private int level;
	private int exp;
	private PlayerLevelSystem playerLevelSystem;
	private String customName;
	private String customMapImage;
	private String customSideImage;
	private int[] lvStats;
	private int movement;
	private int dashMovement;
	private int maxAccessRange;
	private int currentHealth;
	private int exhaustion;
	private AttackMode lastUsed;
	private AttackItemFilter filter;

	public Stats(XClass xClass, int level, String customName, String customMapImage,
			String customSideImage, int movement, PlayerLevelSystem playerLevelSystem)
	{
		this.xClass = xClass;
		this.level = level;
		this.playerLevelSystem = playerLevelSystem;
		this.customName = customName;
		this.customMapImage = customMapImage;
		this.customSideImage = customSideImage;
		lvStats = new int[8];
		autoStats();
		currentHealth = maxHealth();
		this.movement = movement;
		dashMovement = 12;
		maxAccessRange = 4;
		lastUsed = AttackMode.EVADE_MODE;
		filter = new AttackItemFilter(xClass.usableItems);
	}

	public Stats(XClass xClass, int level, PlayerLevelSystem playerLevelSystem)
	{
		this.xClass = xClass;
		this.level = level;
		this.playerLevelSystem = playerLevelSystem;
		lvStats = new int[8];
		autoStats();
		currentHealth = maxHealth();
		movement = xClass.movement;
		dashMovement = 12;
		maxAccessRange = 4;
		lastUsed = AttackMode.EVADE_MODE;
		filter = new AttackItemFilter(xClass.usableItems);
	}

	private Stats(XClass xClass, int level, int exp, String customName, String customMapImage, String customSideImage,
			int[] lvStats, int currentHealth, int exhaustion, int movement, int dashMovement, int maxAccessRange, PlayerLevelSystem playerLevelSystem)
	{
		this.xClass = xClass;
		this.level = level;
		this.exp = exp;
		this.playerLevelSystem = playerLevelSystem;
		this.customName = customName;
		this.customMapImage = customMapImage;
		this.customSideImage = customSideImage;
		this.lvStats = Arrays.copyOf(lvStats, 8);
		this.currentHealth = currentHealth;
		this.exhaustion = exhaustion;
		this.movement = movement;
		this.dashMovement = dashMovement;
		this.maxAccessRange = maxAccessRange;
		lastUsed = AttackMode.EVADE_MODE;
		filter = new AttackItemFilter(xClass.usableItems);
	}

	public Stats copy()
	{
		return new Stats(xClass, level, exp, customName, customMapImage, customSideImage, lvStats,
				currentHealth, exhaustion, movement, dashMovement, maxAccessRange, playerLevelSystem);
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

	public int statByNum(int num)
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

	public int movement()
	{
		return movement;
	}

	public int dashMovement()
	{
		return dashMovement;
	}

	public int maxAccessRange()
	{
		return maxAccessRange;
	}

	public AttackMode lastUsed()
	{
		return lastUsed;
	}

	public void equipMode(AttackMode mode)
	{
		lastUsed = mode;
	}

	public void autoEquip(XCharacter entity)
	{
		ItemView itemView = entity.outputInv().viewRecipeItem(filter);
		if(itemView.base != 0 && itemView.item instanceof AttackItem attackItem)
		{
			lastUsed = attackItem.attackModes().stream().findFirst().orElse(AttackMode.EVADE_MODE);
		}
		else
		{
			lastUsed = AttackMode.EVADE_MODE;
		}
	}

	public void afterTrading(XCharacter entity)
	{
		if(!lastUsed.active)
			return;
		if(!entity.outputInv().canGive(new ItemStack(lastUsed.item, 1), false))
		{
			lastUsed = AttackMode.EVADE_MODE;
		}
	}

	public Item getItemFilter()
	{
		return filter;
	}

	public int getRegenerateChange()
	{
		return maxHealth() - currentHealth;
	}

	public void regenerating()
	{
		exhaustion++;
	}

	public CharSequence getName()
	{
		return customName != null ? customName : new ArgsText(xClass.className + " lv" + level);
	}

	public String mapImageName()
	{
		if(customMapImage != null)
			return customMapImage;
		else
			return "mapsprite.default";
	}

	public String sideImageName()
	{
		if(customSideImage != null)
			return customSideImage;
		else
			return "character.enemy.0";
	}

	public Stats(JrsObject data, ItemLoader itemLoader)
	{
		xClass = XClasses.INSTANCE.xClasses[((JrsNumber) data.get("Class")).getValue().intValue()];
		filter = new AttackItemFilter(xClass.usableItems);
		level = ((JrsNumber) data.get("Level")).getValue().intValue();
		exp = ((JrsNumber) data.get("Exp")).getValue().intValue();
		if(data.get("LevelSystem") != null)
		{
			playerLevelSystem = new PlayerLevelSystem(((JrsObject) data.get("LevelSystem")));
		}
		if(data.get("CustomName") != null)
		{
			customName = data.get("CustomName").asText();
		}
		if(data.get("CustomMapImage") != null)
		{
			customMapImage = data.get("CustomMapImage").asText();
		}
		if(data.get("CustomSideImage") != null)
		{
			customSideImage = data.get("CustomSideImage").asText();
		}
		lvStats = new int[8];
		Iterator<JrsValue> iterator = ((JrsArray) data.get("LvStats")).elements();
		for(int i = 0; i < 8; i++)
		{
			lvStats[i] = ((JrsNumber) iterator.next()).getValue().intValue();
		}
		currentHealth = ((JrsNumber) data.get("CurrentHealth")).getValue().intValue();
		exhaustion = ((JrsNumber) data.get("Exhaustion")).getValue().intValue();
		movement = ((JrsNumber) data.get("Movement")).getValue().intValue();
		dashMovement = ((JrsNumber) data.get("DashMovement")).getValue().intValue();
		maxAccessRange = ((JrsNumber) data.get("MaxAccessRange")).getValue().intValue();
		if(data.get("LastUsed") != null)
		{
			AttackItem item1 = ((AttackItem) itemLoader.loadItem((JrsObject) data.get("LastUsedItem")));
			lastUsed = item1.attackModes().stream().filter(e -> e.mode.code() == ((JrsNumber) data.get("LastUsed")).getValue().intValue())
					.findFirst().orElseThrow();
		}
		else
		{
			lastUsed = AttackMode.EVADE_MODE;
		}
	}

	public <T extends ComposerBase> void save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		a1.put("Class", xClass.code);
		a1.put("Level", level);
		a1.put("Exp", exp);
		if(playerLevelSystem != null)
		{
			playerLevelSystem.save(a1.startObjectField("LevelSystem"));
		}
		if(customName != null)
		{
			a1.put("CustomName", customName);
		}
		if(customMapImage != null)
		{
			a1.put("CustomMapImage", customMapImage);
		}
		if(customSideImage != null)
		{
			a1.put("CustomSideImage", customSideImage);
		}
		var a2 = a1.startArrayField("LvStats");
		for(int i = 0; i < 8; i++)
		{
			a2.add(lvStats[i]);
		}
		a2.end();
		a1.put("CurrentHealth", currentHealth);
		a1.put("Exhaustion", exhaustion);
		a1.put("Movement", movement);
		a1.put("DashMovement", dashMovement);
		a1.put("MaxAccessRange", maxAccessRange);
		if(lastUsed.active)
		{
			a1.put("LastUsed", lastUsed.mode.code());
			itemLoader.saveItem(a1.startObjectField("LastUsedItem"), lastUsed.item, true);
		}
		a1.end();
	}

	public List<? extends CharSequence> info()
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
		info.add("Defend\n" + (lastUsed.active ? lastUsed.item.info().get(0).toString().replace("Type\n", "") : "None")); //TODO
		for(AI2Class e : filter.getItemTypes())
		{
			info.add("ItemType\n" + e.getClass().getSimpleName().replace("Item", ""));
		}
		for(Ability2 ability : xClass.abilities)
		{
			info.add("Ability\n" + ability.name);
		}
		return info;
	}

	public List<? extends CharSequence> statsInfo()
	{
		List<String> info = new ArrayList<>();
		info.add("Class\n" + xClass.className);
		info.add("Level\n" + level);
		info.add("Exp\n" + exp);
		info.add("Health\n" + currentHealth + "/" + maxHealth());
		info.add("Move\n" + movement);
		if(exhaustion > 0)
			info.add("Exhausted\n" + exhaustion);
		else if(exhaustion < 0)
			info.add("Boosted\n" + (-exhaustion));
		else
			info.add("");
		for(int i = 0; i < 8; i++)
		{
			info.add(statNames[i] + "\n" + lvStats[i]);
		}
		info.add("CPower\n" + calcCPower());
		return info;
	}

	public List<? extends CharSequence> infoWithEquip()
	{
		return AttackMode3.convert(this, lastUsed).info();
	}

	public List<? extends CharSequence> levelup()
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
		if(currentHealth > 0)
		{
			if(levelup[7] > 0)
				changedHealth += levelup[7] * HEALTH_MULTIPLIER;
			if(changedHealth > (lvStats[7] + levelup[7]) * HEALTH_MULTIPLIER)
				changedHealth = (lvStats[7] + levelup[7]) * HEALTH_MULTIPLIER;
		}
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
			lvStats[i] += levelup[i];
		}
		currentHealth = changedHealth;
		return info;
	}

	public CharSequence[] sideInfoText(CharSequence generic)
	{
		return new CharSequence[]
				{
						customName != null ? customName : generic,
						new ArgsText("sideinfo.classandlevel", xClass.className, level)
				};
	}

	public List<? extends CharSequence> infoEdit()
	{
		List<CharSequence> info = new ArrayList<>();
		if(customName != null)
			info.add(new ArgsText("stats.edit.customname", customName));
		else
			info.add("stats.edit.genericname");
		info.add(new ArgsText("stats.edit.class", xClass.className));
		info.add(new ArgsText("stats.edit.level", level));
		info.add(new ArgsText("stats.edit.exp", exp));
		for(int i = 0; i < 8; i++)
		{
			info.add(new ArgsText("stats.edit." + statNames[i], lvStats[i]));
		}
		info.add(new ArgsText("stats.edit.health", currentHealth, maxHealth()));
		info.add(new ArgsText("stats.edit.exhaustion", exhaustion));
		info.add(new ArgsText("stats.edit.movement", movement));
		if(lastUsed.active)
			info.add(MultiText.lines("stats.edit.defendwith", lastUsed.item.itemClass.name()));
		else
			info.add("stats.edit.defendwithnone");
		List<CharSequence> m1 = new ArrayList<>();
		m1.add(new ArgsText("stats.edit.itemfilter"));
		filter.getItemTypes().stream().map(AI2Class::name).forEach(m1::add);
		info.add(new MultiText(m1, MultiTextConnect.LINES));
		return info;
	}

	public static List<? extends CharSequence> editOptions(int num)
	{
		if(num == 0)
			return List.of("stats.editoption.name.name", "stats.editoption.name.mapimage", "stats.editoption.name.sideimage");
		if(num == 1)
			return List.of("stats.editoption.class.previous", "stats.editoption.class.next");
		if(num == 2)
			return List.of("stats.editoption.plus", "stats.editoption.minus", "stats.editoption.level.resetstats");
		if(num == 3)
			return List.of("stats.editoption.plus", "stats.editoption.minus", "stats.editoption.exp.reset");
		if(num <= 11)
			return List.of("stats.editoption.plus", "stats.editoption.minus", "stats.editoption.lvstat.reset");
		if(num == 12)
			return List.of("stats.editoption.plus", "stats.editoption.minus", "stats.editoption.health.reset");
		if(num == 13)
			return List.of("stats.editoption.plus", "stats.editoption.minus", "stats.editoption.exhaustion.reset");
		if(num == 14)
			return List.of("stats.editoption.plus", "stats.editoption.minus", "stats.editoption.movement.reset");
		if(num == 15)
			return List.of("stats.editoption.defendwith.autoequip");
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
				File file = new FileChooser().showOpenDialog(null);
				if(file != null)
					customMapImage = file.getName();
				else
					customMapImage = null;
			}
			case 0x02 ->
			{
				File file = new FileChooser().showOpenDialog(null);
				if(file != null)
					customSideImage = file.getName();
				else
					customSideImage = null;
			}
			case 0x10 ->
			{
				xClass = XClasses.INSTANCE.xClasses[xClass.code - 1];
				filter = new AttackItemFilter(xClass.usableItems);
			}
			case 0x11 ->
			{
				xClass = XClasses.INSTANCE.xClasses[xClass.code + 1];
				filter = new AttackItemFilter(xClass.usableItems);
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