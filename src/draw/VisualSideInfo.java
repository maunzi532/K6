package draw;

import entity.*;
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

	public void setSideInfo(SideInfo sideInfo, boolean r)
	{
		if(r)
			r0.setSideInfo(sideInfo);
		else
			l0.setSideInfo(sideInfo);
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

	public void attackInfo(AttackInfo aI)
	{
		boolean inverted = inverted(aI.entity.location(), aI.entityT.location());
		l0.setSideInfo(new SideInfo(aI.getStats(inverted).image(), aI.getInfos(inverted)));
		r0.setSideInfo(new SideInfo(aI.getStats(!inverted).image(), aI.getInfos(!inverted)));
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