package visual1.sideinfo;

import visual1.*;

public class VisualSideInfoFrame
{
	private final VisualSideInfo[] viewers;

	public VisualSideInfoFrame(VisualSideInfo... viewers)
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

	public void draw(Scheme scheme)
	{
		for(int i = 0; i < viewers.length; i++)
		{
			viewers[i].draw(scheme);
		}
	}

	public double takeY2()
	{
		return viewers[1].takeY2();
	}
}