package text;

public record ArgsText(String key, Object... args) implements CharSequence, SLocaleFeature
{
	@Override
	public int length()
	{
		return key.length();
	}

	@Override
	public char charAt(int index)
	{
		return key.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end)
	{
		return key.subSequence(start, end);
	}

	@Override
	public String toString()
	{
		return key;
	}
}