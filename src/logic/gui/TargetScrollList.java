package logic.gui;

import java.util.*;
import java.util.function.*;

public class TargetScrollList<T> extends ScrollList<T>
{
	private T targeted;
	private T targeted2;

	public TargetScrollList(int locationX, int locationY, int sizeX, int sizeY, int elementSizeX, int elementSizeY,
			List<T> elements, Function<T, GuiTile[]> function, Consumer<T> onClick)
	{
		super(locationX, locationY, sizeX, sizeY, elementSizeX, elementSizeY, elements, function, onClick);
		this.onTarget = this::onTarget;
	}

	private boolean onTarget(T target)
	{
		targeted2 = target;
		return false;
	}

	public T getTargeted()
	{
		return targeted;
	}

	@Override
	public ElementTargetResult target(int x, int y, boolean click)
	{
		ElementTargetResult result = super.target(x, y, click);
		if(!click)
		{
			if(targeted2 != null)
			{
				if(targeted != targeted2)
				{
					targeted = targeted2;
					targeted2 = null;
					return new ElementTargetResult(result.inside(), true, result.targetTile());
				}
				else
				{
					targeted2 = null;
					return result;
				}
			}
			else
			{
				if(targeted != null)
				{
					targeted = null;
					return new ElementTargetResult(result.inside(), true, result.targetTile());
				}
				else
				{
					return result;
				}
			}
		}
		else
		{
			return result;
		}
	}
}