package text;

public record LocaleText(CharSequence key) implements CharSequence
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
}