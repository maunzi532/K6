package entity.enemy;

import entity.*;
import entity.hero.*;
import geom.f1.*;
import logic.*;

public class XEnemy extends XEntity
{
	public XEnemy(Tile location, MainState mainState, Stats stats)
	{
		super(location, mainState, stats);
	}

	@Override
	public boolean isEnemy(XEntity other)
	{
		return other instanceof XHero;
	}
}