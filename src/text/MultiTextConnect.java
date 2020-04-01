package text;

public enum MultiTextConnect
{
	SPACES("multitext.spaces"),
	LINES("multitext.lines");

	public final CharSequence text;

	MultiTextConnect(CharSequence text)
	{
		this.text = text;
	}
}