package arrow;

import geom.f1.*;
import java.util.*;
import javafx.scene.image.*;

public class BlinkArrow extends XArrow
{
	private int blinkTime;

	public BlinkArrow(Tile location, int duration, boolean loop, Image image, int blinkTime)
	{
		super(List.of(location), duration, loop, image);
		this.blinkTime = blinkTime;
	}

	@Override
	public Image image()
	{
		return (counter / blinkTime) % 2 == 0 ? image : null;
	}

	@Override
	public boolean zeroUpwards()
	{
		return false;
	}
}