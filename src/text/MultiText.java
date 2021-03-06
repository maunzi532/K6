package text;

import java.util.*;

public record MultiText(List<? extends CharSequence> parts, MultiTextConnect connect) implements CharSequence
{
	public static MultiText lines(CharSequence... parts)
	{
		return new MultiText(List.of(parts), MultiTextConnect.LINES);
	}

	@Override
	public int length()
	{
		return parts.get(0).length();
	}

	@Override
	public char charAt(int index)
	{
		return parts.get(0).charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end)
	{
		return parts.get(0).subSequence(start, end);
	}
}