package arrow;

import geom.f1.*;
import java.util.*;
import javafx.scene.image.*;

public class ManualXArrow extends XArrow
{
	public ManualXArrow(List<Tile> locations, int duration, Image image)
	{
		super(locations, duration, true, image);
	}

	@Override
	public void tick(){}

	public void setCounter(int counter)
	{
		this.counter = counter;
	}
}