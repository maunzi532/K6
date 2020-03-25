package arrow;

import geom.f1.*;
import java.util.*;

public class BlinkArrow extends XArrow
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