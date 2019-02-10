package entity.hero;

import entity.*;
import hex.*;
import inv.*;

public class XHero extends XEntity implements DoubleInv
{
	private Inv2 inv;

	public XHero(Hex location)
	{
		super(location);
	}

	@Override
	public Hex location()
	{
		return location;
	}

	@Override
	public Inv2 inputInv()
	{
		return inv;
	}

	@Override
	public Inv2 outputInv()
	{
		return inv;
	}

	@Override
	public int inputPriority()
	{
		return -1;
	}

	@Override
	public int outputPriority()
	{
		return -1;
	}
}