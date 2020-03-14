package entity;

public class SaveSettings
{
	public boolean startLocked;
	public boolean startInvLocked;

	public SaveSettings(boolean startLocked, boolean startInvLocked)
	{
		this.startLocked = startLocked;
		this.startInvLocked = startInvLocked;
	}

	public static SaveSettings copy(SaveSettings t1)
	{
		if(t1 == null)
			return null;
		else
			return new SaveSettings(t1.startLocked, t1.startInvLocked);
	}
}