package text;

public record KeyFunction(String function) implements CharSequence, SLocaleFeature
{
	@Override
	public int length()
	{
		return function.length();
	}

	@Override
	public char charAt(int index)
	{
		return function.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end)
	{
		return function.subSequence(start, end);
	}
}