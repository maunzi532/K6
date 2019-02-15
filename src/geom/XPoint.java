package geom;

import geom.hex.Hex;
import geom.quad.Quad;

public class XPoint
{
	public final int[] v;

	public XPoint(int x, int y)
	{
		v = new int[]{x, y};
	}

	public XPoint(Quad h1)
	{
		v = new int[]{h1.v[0], h1.v[1]};
	}

	public XPoint(Hex h1)
	{
		v = new int[]{h1.v[0] + h1.v[2] / 2, h1.v[2]};
	}

	public Quad toQuad()
	{
		return new Quad(v[0], v[1]);
	}

	public Hex toHex()
	{
		return new Hex(v[0] - v[1] / 2, v[1]);
	}
}