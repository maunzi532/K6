package building.blueprint;

import file.BlueprintNode;

public class TransporterBlueprint
{
	public final int range;
	public final int amount;

	public TransporterBlueprint(int range, int amount)
	{
		this.range = range;
		this.amount = amount;
	}

	public TransporterBlueprint(BlueprintNode node)
	{
		if(!node.data.equals(getClass().getSimpleName()))
			throw new RuntimeException(node.data + ", required: " + getClass().getSimpleName());
		range = node.get(0).dataInt();
		amount = node.get(1).dataInt();
	}
}