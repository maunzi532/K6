package editor.xgui;

import entity.*;
import geom.tile.*;
import gui.*;
import item.*;
import logic.*;
import system.*;

public final class EntityCreateGUI extends GUIState
{
	private static final AreaTile addXHero = new AreaTile(0, 0, new GuiTile("gui.edit.create.ally"), 2, 1);
	private static final AreaTile addXEnemy = new AreaTile(0, 1, new GuiTile("gui.edit.create.enemy"), 2, 1);

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
		elements.add(new TileElement(addXHero, true, null,
				() -> createXCharacter(true, mainState)));
		elements.add(new TileElement(addXEnemy, true, null,
				() -> createXCharacter(false, mainState)));
		update();
	}

	private void createXCharacter(boolean xHero, MainState mainState)
	{
		SystemChar systemChar = new SystemChar(new EnemyLevelSystem(mainState.worldSettings().allXClasses.get(0), 0),
				new TagInv(10), new EnemyAI(mainState.levelMap().y1().create2(0, 0)), -1);
		XCharacter entity;
		if(xHero)
		{
			entity = new XCharacter(CharacterTeam.HERO, true, 0, location,
					null, null, null, systemChar, false);
		}
		else
		{
			entity = new XCharacter(CharacterTeam.ENEMY, false, 0, location,
					null, null, null, systemChar, false);
		}
		mainState.levelMap().addEntity(entity);
		mainState.stateHolder().setState(new EntityEditGUI(entity));
	}

	@Override
	public int xw()
	{
		return 2;
	}

	@Override
	public int yw()
	{
		return 2;
	}
}