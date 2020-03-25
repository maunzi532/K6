package visual1.sideinfo;

import file.*;

public class VisualSideInfo
{
	private SideInfoViewer[] viewers;

	public VisualSideInfo(SideInfoViewer... viewers)
	{
		this.viewers = viewers;
	}

	public void tick()
	{
		for(int i = 0; i < viewers.length; i++)
		{
			viewers[i].tick();
		}
	}

	public void draw(ColorScheme colorScheme)
	{
		for(int i = 0; i < viewers.length; i++)
		{
			viewers[i].draw(colorScheme);
		}
	}

	public double takeY2()
	{
		return viewers[1].takeY2();
	}
}