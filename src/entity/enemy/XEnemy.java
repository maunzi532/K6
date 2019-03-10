package entity.enemy;

import entity.*;
import entity.hero.*;
import geom.hex.*;

public class XEnemy extends XEntity
{
	public XEnemy(Hex location)
	{
		super(location);
	}

	@Override
	public boolean isEnemy(MEntity other)
	{
		return other instanceof XHero;
	}
}