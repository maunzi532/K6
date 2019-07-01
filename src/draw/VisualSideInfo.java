package draw;

import arrow.*;
import entity.*;
import file.*;
import geom.*;
import geom.f1.*;

public class VisualSideInfo
{
	private final SideInfoViewer l0;
	private final SideInfoViewer r0;
	private final TileCamera tileCamera;

	public VisualSideInfo(XGraphics graphics, TileCamera tileCamera)
	{
		l0 = new SideInfoViewer(graphics, false);
		r0 = new SideInfoViewer(graphics, true);
		this.tileCamera = tileCamera;
	}

	public void setSideInfo(SideInfo sideInfoL, SideInfo sideInfoR)
	{
		l0.setSideInfo(sideInfoL);
		r0.setSideInfo(sideInfoR);
	}

	public void setSideInfoL(SideInfo sideInfoL)
	{
		l0.setSideInfo(sideInfoL);
	}

	public void setSideInfoR(SideInfo sideInfoR)
	{
		r0.setSideInfo(sideInfoR);
	}

	public void clearSideInfo()
	{
		l0.setSideInfo(null);
		r0.setSideInfo(null);
	}

	public void tick()
	{
		l0.tick();
		r0.tick();
	}

	public void draw()
	{
		l0.draw();
		r0.draw();
	}

	public void sidedInfo(XEntity e1, XEntity e2)
	{
		boolean inverted = inverted(e1.location(), e2.location());
		l0.setSideInfo((inverted ? e2 : e1).standardSideInfo());
		r0.setSideInfo((inverted ? e1 : e2).standardSideInfo());
	}

	public void attackInfo(AttackInfo aI)
	{
		boolean inverted = inverted(aI.entity.location(), aI.entityT.location());
		l0.setSideInfo(new SideInfo(aI.getEntity(inverted), 0, ImageLoader.getImage(aI.getStats(inverted).imagePath()),
				StatBar.forEntity(aI.getEntity(inverted)), aI.getSideInfos(inverted)));
		r0.setSideInfo(new SideInfo(aI.getEntity(!inverted), 0, ImageLoader.getImage(aI.getStats(!inverted).imagePath()),
				StatBar.forEntity(aI.getEntity(!inverted)), aI.getSideInfos(!inverted)));
	}

	private boolean inverted(Tile loc0, Tile loc1)
	{
		double[][] gp = tileCamera.layout().polygonCorners(tileCamera.getDoubleType().fromTile(loc0),
				tileCamera.getDoubleType().fromTile(loc1));
		if(gp[0][0] < gp[0][1])
		{
			return false;
		}
		if(gp[0][0] > gp[0][1])
		{
			return true;
		}
		return gp[1][0] <= gp[1][1];
	}
}