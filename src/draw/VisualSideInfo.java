package draw;

import geom.*;

public class VisualSideInfo
{
	private SideInfoViewer l0;
	private SideInfoViewer r0;

	public VisualSideInfo(XGraphics graphics)
	{
		l0 = new SideInfoViewer(graphics, false);
		r0 = new SideInfoViewer(graphics, true);
	}

	public void setSideInfo(SideInfo sideInfo, boolean r)
	{
		if(r)
			r0.setSideInfo(sideInfo);
		else
			l0.setSideInfo(sideInfo);
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
}