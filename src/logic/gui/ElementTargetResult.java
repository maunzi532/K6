package logic.gui;

public class ElementTargetResult
{
	public final boolean inside;
	public final boolean requiresUpdate;
	public final CTile targetTile;

	public ElementTargetResult(boolean inside, boolean requiresUpdate, CTile targetTile)
	{
		this.inside = inside;
		this.targetTile = targetTile;
		this.requiresUpdate = requiresUpdate;
	}
}