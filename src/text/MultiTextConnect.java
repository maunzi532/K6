package text;

public enum MultiTextConnect
{
	SPACES("multitext.spaces"),
	LISTED("multitext.listed"),
	LINES("multitext.lines"),
	OR("multitext.or");

	public final CharSequence text;

	MultiTextConnect(CharSequence text)
	{
		this.text = text;
	}
}