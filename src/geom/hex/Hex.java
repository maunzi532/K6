package geom.hex;

import java.util.*;

public class Hex
{
	private static final Hex[] directions = new Hex[]
			{
					new Hex(1, -1, 0), new Hex(1, 0, -1), new Hex(0, 1, -1),
					new Hex(-1, 1, 0), new Hex(-1, 0, 1), new Hex(0, -1, 1)
			};

	public final int[] v;

	public Hex(int x, int y, int z)
	{
		assert x + y + z == 0;
		v = new int[]{x, y, z};
	}

	public Hex(int x, int z)
	{
		v = new int[]{x, -x - z, z};
	}

	public Hex(int[] v)
	{
		assert v.length == 3;
		assert v[0] + v[1] + v[2] == 0;
		this.v = v;
	}

	public Hex(Hex copy)
	{
		v = new int[3];
		System.arraycopy(copy.v, 0, v, 0, 3);
	}

	public Hex add(Hex h2)
	{
		return new Hex(v[0] + h2.v[0], v[1] + h2.v[1], v[2] + h2.v[2]);
	}

	public Hex subtract(Hex minus)
	{
		return new Hex(v[0] - minus.v[0], v[1] - minus.v[1], v[2] - minus.v[2]);
	}

	public Hex multiply(int scalar)
	{
		return new Hex(v[0] * scalar, v[1] * scalar, v[2] * scalar);
	}

	public int length()
	{
		return (Math.abs(v[0]) + Math.abs(v[1]) + Math.abs(v[2])) / 2;
	}

	public int distance(Hex h2)
	{
		return subtract(h2).length();
	}

	public static Hex directionHex(int direction)
	{
		return directions[Math.floorMod(direction, 6)];
	}

	public Hex neighbor(int direction)
	{
		return add(directionHex(direction));
	}

	public Hex[] neighbors()
	{
		Hex[] arr = new Hex[6];
		for(int i = 0; i < 6; i++)
		{
			arr[i] = neighbor(i);
		}
		return arr;
	}

	public Hex rotate(boolean inverse)
	{
		if(inverse)
			return new Hex(v[1], v[2], v[0]);
		else
			return new Hex(v[2], v[0], v[1]);
	}

	public List<Hex> range(int minRange, int maxRange)
	{
		List<Hex> re = new ArrayList<>();
		if(minRange <= 0)
			re.add(this);
		for(int i = Math.max(minRange, 1); i <= maxRange; i++)
		{
			for(int j = 0; j < 6; j++)
			{
				for(int k = 0; k < i; k++)
				{
					re.add(add(directionHex(j).multiply(i)).add(directionHex(j + 2).multiply(k)));
				}
			}
		}
		return re;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof Hex)) return false;
		Hex hex = (Hex) o;
		return Arrays.equals(v, hex.v);
	}

	@Override
	public int hashCode()
	{
		return (v[0] << 16) + v[2];
	}

	@Override
	public String toString()
	{
		return v[0] + ", " + v[1] + ", " + v[2];
	}
}