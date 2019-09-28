package logic.gui;

import java.util.*;
import java.util.function.*;

public class ScrollList<T>
{
	private final int locationX, locationY;
	private final int sizeX, sizeY;
	private final int elementSizeX, elementSizeY;
	private final int elementCountX;
	private final int elementCountYm0;
	private final int elementCountYm1;
	private final int elementCountYm2;
	private final CTile scrollUp;
	private final CTile scrollDown;
	public List<T> elements;
	private boolean updateGUIFlag;
	private int currentScroll;
	private boolean skipScroll1;
	private int elementLinesY;
	private boolean canScrollUp;
	private boolean canScrollDown;
	private int elementCountY;
	private int shownLinesY;

	public ScrollList(int locationX, int locationY, int sizeX, int sizeY, int elementSizeX, int elementSizeY)
	{
		this.locationX = locationX;
		this.locationY = locationY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.elementSizeX = elementSizeX;
		this.elementSizeY = elementSizeY;
		elementCountX = sizeX / elementSizeX;
		elementCountYm0 = sizeY / elementSizeY;
		elementCountYm1 = (sizeY - 1) / elementSizeY;
		elementCountYm2 = (sizeY - 2) / elementSizeY;
		scrollUp = new CTile(locationX, locationY,
				new GuiTile("Scroll", null, false, null, sizeX, 1), elementSizeX, 1);
		scrollDown = new CTile(locationX, locationY + sizeY - 1,
				new GuiTile("Scroll", null, false, null, sizeX, 1), elementSizeX, 1);
	}

	public boolean readUpdateGUIFlag()
	{
		if(updateGUIFlag)
		{
			updateGUIFlag = false;
			return true;
		}
		else
		{
			return false;
		}
	}

	public void update()
	{
		elementLinesY = elements.size() / elementCountX;
		if(elementLinesY <= elementCountYm0)
		{
			skipScroll1 = false;
			currentScroll = 0;
			canScrollUp = false;
			canScrollDown = false;
		}
		else
		{
			skipScroll1 = elementCountY(elementLinesY, 0) > elementCountY(elementLinesY, 1);
			currentScroll = Math.max(0, Math.min(currentScroll, elementLinesY - elementCountYm1));
			if(skipScroll1 && currentScroll == 1)
				currentScroll = 0;
			canScrollUp = currentScroll > 0;
			canScrollDown = canScrollDownAt(elementLinesY, currentScroll);
		}
		elementCountY = elementCountY(elementLinesY, currentScroll);
		shownLinesY = Math.min(elementLinesY - currentScroll, elementCountY);
	}

	private int calcElementCountY(boolean withScrollUp, boolean withScrollDown)
	{
		if(withScrollUp && withScrollDown)
			return elementCountYm2;
		else if(!withScrollUp && !withScrollDown)
			return elementCountYm0;
		else
			return elementCountYm1;
	}

	private boolean canScrollDownAt(int elementLinesY, int scroll)
	{
		return elementLinesY - scroll > calcElementCountY(scroll > 0, false);
	}

	private int elementCountY(int elementLinesY, int scroll)
	{
		return calcElementCountY(scroll > 0, canScrollDownAt(elementLinesY, scroll));
	}

	private int startY()
	{
		return canScrollUp ? locationY + 1 : locationY;
	}

	public void draw(GuiTile[][] tiles, Function<T, GuiTile[]> function)
	{
		int startY = startY();
		for(int iy = 0; iy < shownLinesY; iy++)
		{
			for(int ix = 0; ix < elementCountX; ix++)
			{
				int elementNum = (iy + currentScroll) * elementCountX + ix;
				if(elementNum < elements.size())
				{
					int elementX = locationX + ix * elementSizeX;
					int elementY = startY + iy * elementSizeY;
					GuiTile[] elementTiles = function.apply(elements.get(elementNum));
					for(int i = 0; i < elementSizeX * elementSizeY; i++)
					{
						tiles[elementX + i % elementSizeX][elementY + i / elementSizeX] = elementTiles[i];
					}
				}
			}
		}
		if(canScrollUp)
			setTile(tiles, scrollUp);
		if(canScrollDown)
			setTile(tiles, scrollDown);
	}

	private void setTile(GuiTile[][] tiles, CTile tile)
	{
		for(int ix = 0; ix < tile.r; ix++)
		{
			for(int iy = 0; iy < tile.d; iy++)
			{
				if(ix == tile.r - 1 && iy == tile.d - 1)
					tiles[tile.x + ix][tile.y + iy] = tile.guiTile;
				else
					tiles[tile.x + ix][tile.y + iy] = tile.other;
			}
		}
	}

	public Optional<CTile> target(int x, int y, boolean scrollClick, Consumer<T> onTarget, Runnable onMissedTarget)
	{
		int xr = x - locationX;
		int yr = y - locationY;
		if(xr < 0 || yr < 0 || xr >= sizeX || yr >= sizeY)
		{
			return Optional.empty();
		}
		if(canScrollUp && yr == 0)
		{
			if(scrollClick)
			{
				currentScroll--;
				if(skipScroll1 && currentScroll == 1)
					currentScroll = 0;
				updateGUIFlag = true;
			}
			if(onMissedTarget != null)
			{
				onMissedTarget.run();
			}
			return Optional.of(scrollUp);
		}
		if(canScrollDown && yr == sizeY - 1)
		{
			if(scrollClick)
			{
				currentScroll++;
				if(skipScroll1 && currentScroll == 1)
					currentScroll = 2;
				updateGUIFlag = true;
			}
			if(onMissedTarget != null)
			{
				onMissedTarget.run();
			}
			return Optional.of(scrollDown);
		}
		int yr1 = y - startY();
		if(xr >= elementSizeX * elementCountX || yr1 >= elementSizeY * shownLinesY)
		{
			if(onMissedTarget != null)
			{
				onMissedTarget.run();
			}
			return Optional.of(CTile.NONE);
		}
		int xel = xr / elementSizeX;
		int yel = yr1 / elementSizeY;
		int elementNum = yel * elementCountX + xel;
		if(elementNum >= elements.size())
		{
			if(onMissedTarget != null)
			{
				onMissedTarget.run();
			}
			return Optional.of(CTile.NONE);
		}
		if(onTarget != null)
		{
			onTarget.accept(elements.get(elementNum));
		}
		return Optional.of(new CTile(locationX + xel * elementSizeX, startY() + yel * elementSizeY, elementSizeX, elementSizeY));
	}
}