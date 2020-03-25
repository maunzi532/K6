package system2;

import system2.content.*;

public class AttackMode
{
	public static final AttackMode EVADE_MODE = new AttackMode();

	public final boolean active;
	public final AttackItem item;
	public final AM2Type mode;

	public AttackMode(AttackItem item, AM2Type mode)
	{
		active = true;
		this.item = item;
		this.mode = mode;
	}

	private AttackMode()
	{
		active = false;
		item = null;
		mode = EvadeMode.INSTANCE;
	}

	public String imageName()
	{
		return item != null ? item.image() : null;
	}
}