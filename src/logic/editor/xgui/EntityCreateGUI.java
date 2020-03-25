package logic.editor.xgui;

import entity.*;
import geom.f1.*;
import item.inv.*;
import java.util.stream.*;
import logic.*;
import logic.gui.*;
import system2.*;
import system2.analysis.*;
import system2.content.*;

public class EntityCreateGUI extends XGUIState
{
	private static final CTile addXHero = new CTile(4, 0, new GuiTile("Add Character"), 2, 1);
	private static final CTile addXEnemy = new CTile(4, 2, new GuiTile("Add Enemy"), 2, 1);

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
		elements.add(new CElement(addXHero, true, null, () -> createXCharacter(mainState, CharacterTeam.HERO)));
		elements.add(new CElement(addXEnemy, true, null, () -> createXCharacter(mainState, CharacterTeam.ENEMY)));
		update();
	}

	private void createXCharacter(MainState mainState, CharacterTeam team)
	{
		Stats stats = defaultStats(team == CharacterTeam.HERO);
		Inv inv = new WeightInv(20);
		XCharacter entity;
		if(team == CharacterTeam.HERO)
		{
			entity = new XCharacter(team, 0, location, stats, inv,
					new NoAI(), new TurnResources(location), new StartingSettings(false, false));

		}
		else
		{
			entity = new XCharacter(team, 0, location, stats, inv,
					new StandardAI(mainState.levelMap), new TurnResources(location), null);

		}
		mainState.levelMap.addEntity(entity);
		mainState.stateHolder.setState(new EntityEditGUI(entity));
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