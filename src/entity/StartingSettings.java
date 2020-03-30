package entity;

public final class StartingSettings
{
	public boolean startLocked;
	public boolean startInvLocked;

	public StartingSettings(boolean startLocked, boolean startInvLocked)
	{
		this.startLocked = startLocked;
		this.startInvLocked = startInvLocked;
	}

	public static StartingSettings copy(StartingSettings t1)
	{
		if(t1 == null)
			return null;
		else
			return new StartingSettings(t1.startLocked, t1.startInvLocked);
	}
}