package text;

public enum MultiTextConnect
{
	SPACES("multitext.spaces"),
	LISTED("multitext.listed"),
	LINES("multitext.lines");

	public final CharSequence text;

	MultiTextConnect(CharSequence text)
	{
		this.text = text;
	}
}