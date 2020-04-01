package text;

public record NameText(String name) implements CharSequence
{
	@Override
	public int length()
	{
		return name.length();
	}

	@Override
	public char charAt(int index)
	{
		return name.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end)
	{
		return name.subSequence(start, end);
	}
}