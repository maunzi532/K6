package visual.sideinfo;

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

	public void draw()
	{
		for(int i = 0; i < viewers.length; i++)
		{
			viewers[i].draw();
		}
	}
}