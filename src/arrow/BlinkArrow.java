package arrow;

import geom.tile.*;
import java.util.*;

public final class BlinkArrow extends XArrow
{
	private final int blinkTime;

	public BlinkArrow(Tile location, int duration, boolean loop, String imageName, int blinkTime)
	{
		super(List.of(location), duration, loop, imageName);
		this.blinkTime = blinkTime;
	}

	@Override
	public String imageName()
	{
		return (counter / blinkTime) % 2 == 0 ? imageName : null;
	}

	@Override
	public boolean zeroUpwards()
	{
		return false;
	}
}