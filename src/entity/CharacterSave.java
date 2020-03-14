package entity;

public class CharacterSave
{
	private boolean startLocked;
	private boolean startInvLocked;

	public CharacterSave(boolean startLocked, boolean startInvLocked)
	{
		this.startLocked = startLocked;
		this.startInvLocked = startInvLocked;
	}

	public static CharacterSave copy(CharacterSave t1)
	{
		if(t1 == null)
			return null;
		else
			return new CharacterSave(t1.startLocked, t1.startInvLocked);
	}
}