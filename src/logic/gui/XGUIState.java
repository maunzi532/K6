package logic.gui;

import java.util.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import logic.editor.xstate.*;
import logic.xstate.*;

public abstract class XGUIState implements NState
{
	public static final Image ARROW = new Image("Arrow.png");
	private static final Color BACKGROUND = Color.color(0.4, 0.4, 0.5);

	public GuiTile[][] tiles;
	public List<GuiElement> elements;
	public CTile targeted = CTile.NONE;

	public XGUIState()
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

	public CTile getTargeted()
	{
		return targeted;
	}

	public void setTargeted(CTile targeted)
	{
		this.targeted = targeted;
	}

	public abstract int xw();

	public abstract int yw();

	public Color background()
	{
		return BACKGROUND;
	}

	public void target(int x, int y)
	{
		boolean requireUpdate = false;
		boolean found = false;
		for(GuiElement element : elements)
		{
			ElementTargetResult result = element.target(x, y, false);
			if(result.inside)
			{
				found = true;
				targeted = result.targetTile;
			}
			if(result.requiresUpdate)
			{
				requireUpdate = true;
			}
		}
		if(!found)
			targeted = CTile.NONE;
		if(requireUpdate)
			update();
	}

	public void click(int x, int y, int key, XStateHolder stateHolder)
	{
		for(GuiElement element : elements)
		{
			ElementTargetResult result = element.target(x, y, true);
			if(result.inside)
			{
				if(result.requiresUpdate)
				{
					update();
				}
				break;
			}
		}
	}

	public void clickOutside(int key, XStateHolder stateHolder)
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