package entity.hero;

import entity.*;
import hex.*;
import inv.*;

public class XHero extends XEntity implements DoubleInv
{
	private Inv3 inv;

	public XHero(Hex location)
	{
		super(location);
		inv = new WeightInv3(20);
	}

	@Override
	public Hex location()
	{
		return location;
	}

	@Override
	public Inv3 inputInv()
	{
		return inv;
	}

	@Override
	public Inv3 outputInv()
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