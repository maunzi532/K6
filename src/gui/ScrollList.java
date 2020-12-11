package gui;

import java.util.*;
import java.util.function.*;

public class ScrollList<T> implements GuiElement
{
	private final int locationX, locationY;
	private final int sizeX, sizeY;
	private final int elementSizeX, elementSizeY;
	private final Function<? super T, GuiTile[]> function;
	protected T targetedElement;
	private final Consumer<? super T> onClick;
	private final int elementCountX;
	private final int elementCountYm0;
	private final int elementCountYm1;
	private final int elementCountYm2;
	private final AreaTile scrollUp;
	private final AreaTile scrollDown;
	public List<? extends T> elements;
	private int currentScroll;
	private boolean skipScroll1;
	private boolean canScrollUp;
	private boolean canScrollDown;
	private int shownLinesY;

	public ScrollList(int locationX, int locationY, int sizeX, int sizeY, int elementSizeX, int elementSizeY, List<? extends T> elements,
			Function<? super T, GuiTile[]> function, Consumer<? super T> onClick)
	{
		this.locationX = locationX;
		this.locationY = locationY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.elementSizeX = elementSizeX;
		this.elementSizeY = elementSizeY;
		this.elements = elements;
		this.function = function;
		this.onClick = onClick;
		elementCountX = sizeX / elementSizeX;
		elementCountYm0 = sizeY / elementSizeY;
		elementCountYm1 = (sizeY - 1) / elementSizeY;
		elementCountYm2 = (sizeY - 2) / elementSizeY;
		scrollUp = new AreaTile(locationX, locationY,
				new GuiTile("gui.scroll.up", null, false, null, sizeX, 1), sizeX, 1);
		scrollDown = new AreaTile(locationX, locationY + sizeY - 1,
				new GuiTile("gui.scroll.down", null, false, null, sizeX, 1), sizeX, 1);
	}

	@Override
	public void update()
	{
		int elementLinesY = -Math.floorDiv(-elements.size(), elementCountX);
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
		int elementCountY = elementCountY(elementLinesY, currentScroll);
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

	@Override
	public void draw(GuiTile[][] tiles)
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
			setFilledTile(tiles, scrollUp);
		if(canScrollDown)
			setFilledTile(tiles, scrollDown);
	}

	private void setFilledTile(GuiTile[][] tiles, AreaTile tile)
	{
		for(int ix = 0; ix < tile.right; ix++)
		{
			for(int iy = 0; iy < tile.down; iy++)
			{
				if(ix == tile.right - 1 && iy == tile.down - 1)
					tiles[tile.x + ix][tile.y + iy] = tile.guiTile;
				else
					tiles[tile.x + ix][tile.y + iy] = tile.other;
			}
		}
	}

	@Override
	public ElementTargetResult target(int x, int y, boolean click)
	{
		int xr = x - locationX;
		int yr = y - locationY;
		if(xr < 0 || yr < 0 || xr >= sizeX || yr >= sizeY)
		{
			return new ElementTargetResult(false, false, AreaTile.NONE);
		}
		if(canScrollUp && yr == 0)
		{
			if(click)
			{
				currentScroll--;
				if(skipScroll1 && currentScroll == 1)
					currentScroll = 0;
			}
			return new ElementTargetResult(true, click, scrollUp);
		}
		if(canScrollDown && yr == sizeY - 1)
		{
			if(click)
			{
				currentScroll++;
				if(skipScroll1 && currentScroll == 1)
					currentScroll = 2;
			}
			return new ElementTargetResult(true, click, scrollDown);
		}
		int yr1 = y - startY();
		if(xr >= elementSizeX * elementCountX || yr1 >= elementSizeY * shownLinesY)
		{
			return new ElementTargetResult(true, false, AreaTile.NONE);
		}
		int xel = xr / elementSizeX;
		int yel = yr1 / elementSizeY;
		int elementNum = (yel + currentScroll) * elementCountX + xel;
		if(elementNum >= elements.size())
		{
			return new ElementTargetResult(true, false, AreaTile.NONE);
		}
		boolean requireUpdate = false;
		T targetElement = elements.get(elementNum);
		if(click)
		{
			if(onClick != null)
			{
				onClick.accept(targetElement);
				requireUpdate = true;
			}
		}
		else
		{
			targetedElement = targetElement;
		}
		return new ElementTargetResult(true, requireUpdate,
				new AreaTile(locationX + xel * elementSizeX, startY() + yel * elementSizeY, elementSizeX, elementSizeY));
	}
}