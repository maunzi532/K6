package logic.editor.xgui;

import entity.*;
import geom.tile.*;
import item.inv.*;
import java.util.stream.*;
import levelmap.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;
import statsystem.*;
import statsystem.analysis.*;
import statsystem.content.*;

public final class EntityCreateGUI extends XGUIState
{
	private static final CTile addXHero = new CTile(4, 0, new GuiTile("gui.edit.create.ally"), 2, 1);
	private static final CTile addXEnemy = new CTile(4, 2, new GuiTile("gui.edit.create.enemy"), 2, 1);

	private final Tile location;

	public EntityCreateGUI(Tile location)
	{
		this.location = location;
	}

	@Override
	public boolean editMode()
	{
		return true;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		elements.add(new CElement(addXHero, true, null,
				() -> createXCharacter(CharacterTeam.HERO, mainState.levelMap(), mainState.stateHolder())));
		elements.add(new CElement(addXEnemy, true, null,
				() -> createXCharacter(CharacterTeam.ENEMY, mainState.levelMap(), mainState.stateHolder())));
		update();
	}

	private void createXCharacter(CharacterTeam team, LevelMap levelMap, XStateHolder stateHolder)
	{
		Stats stats = defaultStats(team == CharacterTeam.HERO);
		Inv inv = new WeightInv(20);
		XCharacter entity;
		if(team == CharacterTeam.HERO)
		{
			entity = new XCharacter(CharacterTeam.HERO, 0, location, stats, inv,
					new NoAI(), new TurnResources(location), new StartingSettings(false, false));
		}
		else
		{
			entity = new XCharacter(team, 0, location, stats, inv,
					new StandardAI(levelMap), new TurnResources(location), null);
		}
		levelMap.addEntity(entity);
		stateHolder.setState(new EntityEditGUI(entity));
	}

	private static Stats defaultStats(boolean xh)
	{
		return new Stats(XClasses.mageClass(), 0, xh ? new PlayerLevelSystem(0, IntStream.range(0, 8).toArray(), 40) : null);
	}

	@Override
	public int xw()
	{
		return 6;
	}

	@Override
	public int yw()
	{
		return 4;
	}
}