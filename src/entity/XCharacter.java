package entity;

import arrow.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import doubleinv.*;
import geom.tile.*;
import item4.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import levelmap.*;
import load.*;
import statsystem.*;
import system4.*;
import text.*;

public final class XCharacter implements InvHolder, XSaveableYS
{
	private CharacterTeam team;
	private boolean savedInTeam;
	private int startingDelay;
	private Tile location;
	private NameText customName;
	private String customMapImage;
	private String customSideImage;
	private final SystemChar systemChar;
	private boolean hasMainAction;
	private XArrow visualReplaced;

	public XCharacter(CharacterTeam team, boolean savedInTeam, int startingDelay, Tile location, NameText customName, String customMapImage,
			String customSideImage, SystemChar systemChar, boolean hasMainAction)
	{
		this.team = team;
		this.savedInTeam = savedInTeam;
		this.startingDelay = startingDelay;
		this.location = location;
		this.customName = customName;
		this.customMapImage = customMapImage;
		this.customSideImage = customSideImage;
		this.systemChar = systemChar;
		this.hasMainAction = hasMainAction;
		visualReplaced = null;
	}

	public XCharacter createACopy(Tile copyLocation)
	{
		XCharacter copy = new XCharacter(team, savedInTeam, startingDelay, copyLocation, customName, customMapImage, customSideImage,
				systemChar /*TODO copy*/, hasMainAction);
		return copy;
	}

	public CharacterTeam team()
	{
		return team;
	}

	public boolean isSavedInTeam()
	{
		return savedInTeam;
	}

	public void startTurn()
	{
		if(startingDelay > 0)
			startingDelay--;
	}

	public boolean targetable()
	{
		return startingDelay <= 0;
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

	public StatBar hpBar()
	{
		return new StatBar(team.healthBarColor, "arrow.healthbar.background", "arrow.healthbar.text", currentHP(), maxHP());
	}

	public StatBar hpBar(int overrideHP)
	{
		return new StatBar(team.healthBarColor, "arrow.healthbar.background", "arrow.healthbar.text", overrideHP, maxHP());
	}

	public int movement()
	{
		return systemChar.stat(Stats4.MOVEMENT);
	}

	public int accessRange()
	{
		return systemChar.stat(Stats4.ACCESS_RANGE);
	}

	public List<Integer> enemyTargetRanges(boolean enemy)
	{
		return systemChar.enemyTargetRanges(enemy, !enemy);
	}

	public List<Integer> allyTargetRanges()
	{
		return systemChar.allyTargetRanges();
	}

	public List<AttackCalc4> attackOptions(int distance, XCharacter target)
	{
		return systemChar.possibleAttackItems(distance, true, false).stream()
				.flatMap(e -> AttackInfo4.attackOptions(this, target, distance, true, false)
						.stream().map(AttackCalc4::new)).collect(Collectors.toList());
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
	public void afterTrading()
	{

	}

	public void setHP(int hp)
	{
		systemChar.setCurrentHP(hp);
	}

	public boolean hasMainAction()
	{
		return hasMainAction;
	}

	public void setHasMainAction(boolean hasMainAction)
	{
		this.hasMainAction = hasMainAction;
	}

	public static XCharacter load(JrsObject data, TileType y1, SystemScheme systemScheme)
	{
		CharacterTeam team = CharacterTeam.valueOf(data.get("Team").asText());
		boolean savedInTeam = LoadHelper.asBoolean(data.get("SavedInTeam"));
		int startingDelay = LoadHelper.asInt(data.get("StartingDelay"));
		Tile location = XSaveableY.loadLocation(data, y1);
		NameText customName = new NameText(LoadHelper.asOptionalString(data.get("CustomName")));
		String customMapImage = LoadHelper.asOptionalString(data.get("CustomMapImage"));
		String customSideImage = LoadHelper.asOptionalString(data.get("CustomSideImage"));
		SystemChar systemChar = SystemChar.load(data, y1, systemScheme);
		//TurnResources resources = TurnResources.load((JrsObject) data.get("Resources"), y1);
		boolean hasMainAction = LoadHelper.asBoolean(data.get("HasMainAction"));
		return new XCharacter(team, savedInTeam, startingDelay, location, customName, customMapImage, customSideImage, systemChar, hasMainAction);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, TileType y1, SystemScheme systemScheme) throws IOException
	{
		a1.put("Team", team.name());
		a1.put("SavedInTeam", savedInTeam);
		a1.put("StartingDelay", startingDelay);
		XSaveableY.saveLocation(location, a1, y1);
		if(customName != null)
			a1.put("CustomName", customName);
		if(customMapImage != null)
			a1.put("CustomMapImage", customMapImage);
		if(customSideImage != null)
			a1.put("CustomSideImage", customSideImage);
		systemChar.save(a1, y1, systemScheme);
		//XSaveableY.saveObject("Resources", resources, a1, y1);
		a1.put("HasMainAction", hasMainAction);
	}
}