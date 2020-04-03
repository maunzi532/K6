package entity;

import com.fasterxml.jackson.jr.ob.comp.*;
import geom.tile.*;
import item.*;
import java.io.*;

public final class NoAI implements EnemyAI
{
	@Override
	public EnemyMove preferredMove(XCharacter user, boolean canMove, boolean hasToMove, int moveAway)
	{
		return new EnemyMove(user, -1, null, null, 0);
	}

	@Override
	public EnemyAI copy()
	{
		return new NoAI();
	}

	@Override
	public <T extends ComposerBase> void save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1)
			throws IOException
	{
		a1.put("Type", "NoAI");
		a1.end();
	}
}