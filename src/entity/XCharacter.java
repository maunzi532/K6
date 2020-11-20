package entity;

import arrow.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import doubleinv.*;
import geom.tile.*;
import item.*;
import item4.*;
import java.io.*;
import java.util.*;
import levelmap.*;
import statsystem.*;
import system4.*;
import text.*;

public final class XCharacter implements InvHolder
{
	private CharacterTeam team;
	private int startingDelay;
	private Tile location;
	private NameText customName;
	private String customMapImage;
	private String customSideImage;
	/*private final Stats stats;
	private final Inv inv;
	private EnemyAI enemyAI;*/
	private final SystemChar systemChar;
	private TurnResources resources;
	private XArrow visualReplaced;
	private boolean defeated;

	public XCharacter(CharacterTeam team, int startingDelay, Tile location, NameText customName, String customMapImage,
			String customSideImage, SystemChar systemChar/*Stats stats, Inv inv, EnemyAI enemyAI*/, boolean unlimitedTR)
	{
		this.team = team;
		this.startingDelay = startingDelay;
		this.location = location;
		this.customName = customName;
		this.customMapImage = customMapImage;
		this.customSideImage = customSideImage;
		this.systemChar = systemChar;
		/*this.stats = stats;
		this.inv = inv;
		this.enemyAI = enemyAI;*/
		if(unlimitedTR)
		{
			resources = TurnResources.unlimited();
		}
		else
		{
			resources = new TurnResources(location);
		}
		visualReplaced = null;
		defeated = false;
	}

	public XCharacter createACopy(Tile copyLocation, boolean unlimitedTR)
	{
		XCharacter copy = new XCharacter(team, startingDelay, copyLocation, customName, customMapImage, customSideImage,
				/*stats.createACopy(), inv.copy(), enemyAI.copy()*/systemChar /*TODO copy*/, unlimitedTR);
		//copy.stats().autoEquip(copy);
		return copy;
	}

	public CharacterTeam team()
	{
		return team;
	}

	public void startTurn()
	{
		if(startingDelay > 0)
			startingDelay--;
	}

	public boolean targetable()
	{
		return !defeated && startingDelay <= 0;
	}

	public void setDefeated()
	{
		defeated = true;
	}

	public void setLocation(Tile location)
	{
		this.location = location;
	}

	@Override
	public CharSequence name()
	{
		return customName != null ? customName : systemChar.nameAddedText();
	}

	public CharSequence name1()
	{
		return customName != null ? customName : team().genericName;
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

	public CharSequence[] sideInfoText(CharSequence name)
	{
		return new CharSequence[]{name, systemChar.nameAddedText()};
	}

	public boolean isVisible()
	{
		return visualReplaced == null || visualReplaced.finished();
	}

	public void replaceVisual(XArrow arrow)
	{
		visualReplaced = arrow;
	}

	public Stats stats()
	{
		return null;
	}

	public SystemChar systemChar()
	{
		return systemChar;
	}

	public int currentHP()
	{
		return systemChar.currentHP();
	}

	public int maxHP()
	{
		return systemChar.stat(Stats4.MAX_HP);
	}

	public int movement()
	{
		return systemChar.stat(Stats4.MOVEMENT);
	}

	public int accessRange()
	{
		return systemChar.stat(Stats4.ACCESS_RANGE);
	}

	public List<Integer> attackRanges()
	{
		return systemChar.attackRanges();
	}

	public TurnResources resources()
	{
		return resources;
	}

	public void newResources(TurnResources newResources)
	{
		resources = newResources;
	}

	public boolean isEnemy(XCharacter other)
	{
		return other.team() != team;
	}

	@Override
	public Tile location()
	{
		return location;
	}

	@Override
	public Inv4 inv()
	{
		return systemChar.inv();
	}

	@Override
	public boolean active()
	{
		return !defeated;
	}

	@Override
	public void afterTrading()
	{

	}

	/*public EnemyMove preferredMove(boolean hasToMove, int moveAway)
	{
		//if(!resources.ready())
			return new EnemyMove(this, -1, null, null, 0);
		//return enemyAI.preferredMove(this, resources.hasMoveAction(), hasToMove, moveAway);
	}*/

	public <T extends ComposerBase> void save(ObjectComposer<T> a1, TileType y1) throws IOException
	{
		a1.put("Type", team.name());
		a1.put("sx", y1.sx(location));
		a1.put("sy", y1.sy(location));
		a1.put("StartingDelay", startingDelay);
		a1.end();
	}

	public static XCharacter loadFromMap(JrsObject data, ItemLoader itemLoader, LevelMap levelMap)
	{
		CharacterTeam team = CharacterTeam.valueOf(data.get("Team").asText());
		int startingDelay = ((JrsNumber) data.get("StartingDelay")).getValue().intValue();
		Tile location = levelMap.y1.create2(((JrsNumber) data.get("sx")).getValue().intValue(), ((JrsNumber) data.get("sy")).getValue().intValue());
		NameText customName = data.get("CustomName") != null ? new NameText(data.get("CustomName").asText()) : null;
		String customMapImage = data.get("CustomMapImage") != null ? data.get("CustomMapImage").asText() : null;
		String customSideImage = data.get("CustomSideImage") != null ? data.get("CustomSideImage").asText() : null;
		/*Stats stats = new Stats(((JrsObject) data.get("Stats")), itemLoader);
		Inv inv = new WeightInv(((JrsObject) data.get("Inventory")), itemLoader);
		EnemyAI enemyAI = EnemyAI.load((JrsObject) data.get("EnemyAI"), itemLoader, levelMap);*/
		SystemChar systemChar = new SystemChar();
		return new XCharacter(team, startingDelay, location, customName, customMapImage, customSideImage, systemChar/*stats, inv, enemyAI*/, false);
	}

	public <T extends ComposerBase> void saveToMap(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws IOException
	{
		a1.put("Team", team.name());
		a1.put("StartingDelay", startingDelay);
		a1.put("sx", y1.sx(location));
		a1.put("sy", y1.sy(location));
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
		/*stats.save(a1.startObjectField("Stats"), itemLoader);
		inv.save(a1.startObjectField("Inventory"), itemLoader);
		enemyAI.save(a1.startObjectField("EnemyAI"), itemLoader, y1);*/
		//systemChar.save()
	}

	public static XCharacter loadFromTeam(JrsObject data, int startingDelay, Tile defaultStart, Inv4 invOverride, Inv4 storageInv, ItemLoader itemLoader, LevelMap levelMap)
	{
		CharacterTeam team = CharacterTeam.valueOf(data.get("Team").asText());
		Tile location;
		if(data.get("sx") != null && data.get("sy") != null)
		{
			location = levelMap.y1.create2(((JrsNumber) data.get("sx")).getValue().intValue(), ((JrsNumber) data.get("sy")).getValue().intValue());
		}
		else
		{
			location = defaultStart;
		}
		NameText customName = data.get("CustomName") != null ? new NameText(data.get("CustomName").asText()) : null;
		String customMapImage = data.get("CustomMapImage") != null ? data.get("CustomMapImage").asText() : null;
		String customSideImage = data.get("CustomSideImage") != null ? data.get("CustomSideImage").asText() : null;
		/*Stats stats = new Stats(((JrsObject) data.get("Stats")), itemLoader);
		Inv inv;
		if(invOverride != null)
		{
			Inv toStorage = new WeightInv(((JrsObject) data.get("Inventory")), itemLoader);
			storageInv.tryAdd(toStorage.allItems());
			inv = invOverride;
		}
		else
		{
			inv = new WeightInv(((JrsObject) data.get("Inventory")), itemLoader);
		}
		EnemyAI enemyAI = EnemyAI.load((JrsObject) data.get("EnemyAI"), itemLoader, levelMap);*/
		SystemChar systemChar = new SystemChar();
		return new XCharacter(team, startingDelay, location, customName, customMapImage, customSideImage, systemChar/*stats, inv, enemyAI*/, true);
	}

	public <T extends ComposerBase> void saveToTeam(ObjectComposer<T> a1, boolean saveLocation, boolean canTrade, ItemLoader itemLoader, TileType y1) throws IOException
	{
		a1.put("Team", team.name());
		if(saveLocation)
		{
			a1.put("sx", y1.sx(location));
			a1.put("sy", y1.sy(location));
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
		/*stats.save(a1.startObjectField("Stats"), itemLoader);
		if(canTrade)
		{
			inv.save(a1.startObjectField("Inventory"), itemLoader);
		}
		else
		{
			new WeightInv(inv.viewInvWeight().limit).save(a1.startObjectField("Inventory"), itemLoader);
		}
		enemyAI.save(a1.startObjectField("EnemyAI"), itemLoader, y1);*/
		//systemChar.save()
	}
}