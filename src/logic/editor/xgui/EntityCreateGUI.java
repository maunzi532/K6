package logic.editor.xgui;

import entity.*;
import geom.f1.*;
import item.inv.*;
import logic.*;
import logic.gui.*;
import system2.*;

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
		elements.add(new CElement(addXHero, true, null, () -> createXHero(mainState)));
		elements.add(new CElement(addXEnemy, true, null, () -> createXEnemy(mainState)));
		update();
	}

	private void createXHero(MainState mainState)
	{
		Stats stats = mainState.combatSystem.defaultStats(true);
		Inv inv = new WeightInv(20);
		XCharacter entity = new XCharacter(CharacterTeam.HERO, 0, location, stats, inv,
				null, new TurnResources(location), new SaveSettings(false, false));
		mainState.levelMap.addEntity(entity);
		mainState.stateHolder.setState(new EntityEditGUI(entity));
	}

	private void createXEnemy(MainState mainState)
	{
		Stats stats = mainState.combatSystem.defaultStats(false);
		EnemyAI standardAI = mainState.combatSystem.standardAI();
		Inv inv = new WeightInv(20);
		XCharacter entity = new XCharacter(CharacterTeam.ENEMY, 0, location, stats, inv,
				standardAI, new TurnResources(location), null);
		mainState.levelMap.addEntity(entity);
		mainState.stateHolder.setState(new EntityEditGUI(entity));
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