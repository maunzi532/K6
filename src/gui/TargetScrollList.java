package gui;

import java.util.*;
import java.util.function.*;

public final class TargetScrollList<T> extends ScrollList<T>
{
	private T targeted;

	public TargetScrollList(int locationX, int locationY, int sizeX, int sizeY, int elementSizeX, int elementSizeY,
			List<? extends T> elements, Function<? super T, GuiTile[]> function, Consumer<? super T> onClick)
	{
		super(locationX, locationY, sizeX, sizeY, elementSizeX, elementSizeY, elements, function, onClick);
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
			if(targetedElement != null)
			{
				if(targeted != targetedElement)
				{
					targeted = targetedElement;
					targetedElement = null;
					return new ElementTargetResult(result.inside(), true, result.targetTile());
				}
				else
				{
					targetedElement = null;
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