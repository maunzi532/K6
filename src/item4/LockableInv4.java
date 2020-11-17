package item4;

public class LockableInv4 implements Inv4
{
	//TODO

	@Override
	public boolean canAddAll(Item4 addItem, int addCount)
	{
		return false;
	}

	@Override
	public boolean tryAdd(Item4 addItem, int addCount)
	{
		return false;
	}
}