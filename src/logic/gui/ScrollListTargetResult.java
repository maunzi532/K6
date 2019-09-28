package logic.gui;

public class ScrollListTargetResult<T>
{
	public final boolean inside;
	public final boolean scrolled;
	public final CTile targetTile;
	public final T target;

	public ScrollListTargetResult(boolean inside, boolean scrolled, CTile targetTile, T target)
	{
		this.inside = inside;
		this.targetTile = targetTile;
		this.scrolled = scrolled;
		this.target = target;
	}
}