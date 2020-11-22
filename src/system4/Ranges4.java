package system4;

public class Ranges4
{
	public Ranges4(String data)
	{

	}

	public static Ranges4 load(String data)
	{
		if(data != null)
			return new Ranges4(data);
		else
			return null;
	}

	public boolean hasRange(int range, int rangeBonus)
	{
		return false;
	}
}