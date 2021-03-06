package gui;

import java.util.*;
import logic.*;
import editor.xstate.*;
import xstate.*;

public abstract class GUIState implements NState
{
	public GuiTile[][] tiles;
	protected List<GuiElement> elements;
	public AreaTile targeted = AreaTile.NONE;

	protected GUIState()
	{
		elements = new ArrayList<>();
	}

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}

	protected void update()
	{
		updateBeforeDraw();
		initTiles();
		for(GuiElement element : elements)
		{
			element.update();
			element.draw(tiles);
		}
		updateAfterDraw();
	}

	protected void updateBeforeDraw(){}

	protected void updateAfterDraw(){}

	protected void initTiles()
	{
		tiles = new GuiTile[xw()][yw()];
		for(int i = 0; i < xw(); i++)
		{
			for(int j = 0; j < yw(); j++)
			{
				tiles[i][j] = GuiTile.EMPTY;
			}
		}
	}

	public AreaTile getTargeted()
	{
		return targeted;
	}

	protected void setTargeted(AreaTile targeted)
	{
		this.targeted = targeted;
	}

	public abstract int xw();

	public abstract int yw();

	public void target(int x, int y)
	{
		boolean requireUpdate = false;
		boolean found = false;
		for(GuiElement element : elements)
		{
			ElementTargetResult result = element.target(x, y, false);
			if(result.inside())
			{
				found = true;
				targeted = result.targetTile();
			}
			if(result.requiresUpdate())
			{
				requireUpdate = true;
			}
		}
		if(!found)
			targeted = AreaTile.NONE;
		if(requireUpdate)
			update();
	}

	public void click(int x, int y, XKey key, XStateHolder stateHolder)
	{
		for(GuiElement element : elements)
		{
			ElementTargetResult result = element.target(x, y, true);
			if(result.inside())
			{
				if(result.requiresUpdate())
				{
					update();
				}
				break;
			}
		}
	}

	public void clickOutside(XKey key, XStateHolder stateHolder)
	{
		close(stateHolder);
	}

	public void close(XStateHolder stateHolder)
	{
		if(editMode())
			stateHolder.setState(EditingState.INSTANCE);
		else
			stateHolder.setState(NoneState.INSTANCE);
	}
}