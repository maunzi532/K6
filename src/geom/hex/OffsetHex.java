package geom.hex;

public class OffsetHex
{
	public final int[] v;

	public OffsetHex(int x, int y)
	{
		v = new int[]{x, y};
	}

	public OffsetHex(Hex h1)
	{
		v = new int[]{h1.v[0] + h1.v[2] / 2, h1.v[2]};
	}

	public Hex toHex()
	{
		return new Hex(v[0] - v[1] / 2, v[1]);
	}
}