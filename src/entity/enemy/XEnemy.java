package entity.enemy;

import entity.*;
import entity.hero.*;
import geom.f1.*;

public class XEnemy extends XEntity
{
	public XEnemy(Tile location)
	{
		super(location);
	}

	@Override
	public boolean isEnemy(XEntity other)
	{
		return other instanceof XHero;
	}
}