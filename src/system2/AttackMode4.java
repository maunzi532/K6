package system2;

import java.util.*;
import javafx.scene.image.*;
import system2.content.*;

public class AttackMode4
{
	public static final AttackMode4 EVADE_MODE = new AttackMode4();

	public final boolean active;
	public final AttackItem2 item;
	public final AM2Type mode;

	public AttackMode4(AttackItem2 item, AM2Type mode)
	{
		active = true;
		this.item = item;
		this.mode = mode;
	}

	private AttackMode4()
	{
		active = false;
		item = null;
		mode = EvadeMode.INSTANCE;
	}

	public AttackMode4 shortVersion()
	{
		return this;
	}

	public Image image()
	{
		return item != null ? item.image() : null;
	}

	public String tile()
	{
		return null;
	}

	public List<String> modeInfo()
	{
		return null;
	}
}