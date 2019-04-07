package arrow;

import geom.d1.*;
import geom.f1.*;
import java.util.*;
import javafx.scene.image.*;

public class ShineArrow extends XArrow
{
	private final boolean hasShine;
	private DoubleTile[] stored;

	public ShineArrow(List<Tile> locations, int duration, boolean loop, Image image, boolean hasShine)
	{
		super(locations, duration, loop, image);
		this.hasShine = hasShine;
	}

	@Override
	public boolean hasShine()
	{
		return hasShine;
	}

	@Override
	public void storeArrowPoints(DoubleTile[] store)
	{
		stored = store;
	}

	@Override
	public DoubleTile[] storedArrowPoints()
	{
		return stored;
	}
}