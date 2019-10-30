package system2;

import entity.*;
import java.util.*;
import javafx.scene.image.*;

public class AttackMode4 implements XMode
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
		mode = null;
	}

	@Override
	public XMode shortVersion()
	{
		return this;
	}

	@Override
	public Image image()
	{
		return item != null ? item.image() : null;
	}

	@Override
	public String tile()
	{
		return null;
	}

	@Override
	public List<String> modeInfo()
	{
		return null;
	}
}