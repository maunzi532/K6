package system;

import java.util.*;
import java.util.stream.*;

public class Ranges4
{
	private final List<Integer> ranges;

	public Ranges4(String data)
	{
		ranges = Arrays.stream(data.split(","))
				.filter(e -> !e.isBlank()).map(Integer::parseInt).collect(Collectors.toList());
	}

	public static Ranges4 load(String data)
	{
		if(data != null)
			return new Ranges4(data);
		else
			return null;
	}

	public List<Integer> ranges()
	{
		return ranges;
	}

	public boolean hasRange(int range, int rangeBonus)
	{
		for(int i = 0; i <= rangeBonus; i++)
		{
			if(ranges.contains(range - i))
				return true;
		}
		return false;
	}

	public IntStream ranges(int rangeBonus)
	{
		return IntStream.range(0, rangeBonus + 1)
				.flatMap(i -> ranges.stream().mapToInt(i1 -> i1 + i)).distinct().sorted();
	}
}