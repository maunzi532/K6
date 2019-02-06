package building.blueprint;

import file.*;

public class BuildingBlueprint
{
	public final String name;
	public final ConstructionBlueprint constructionBlueprint;
	public final ProductionBlueprint productionBlueprint;

	public BuildingBlueprint(String name, ConstructionBlueprint constructionBlueprint,
			ProductionBlueprint productionBlueprint)
	{
		this.name = name;
		this.constructionBlueprint = constructionBlueprint;
		this.productionBlueprint = productionBlueprint;
	}

	public BuildingBlueprint(String filename)
	{
		this(new FileBlueprint(BuildingBlueprint.class, filename).startNode);
	}

	public BuildingBlueprint(BlueprintNode node)
	{
		if(!node.data.equals(getClass().getSimpleName()))
			throw new RuntimeException(node.data + ", required: " + getClass().getSimpleName());
		name = node.get(0).data;
		constructionBlueprint = new ConstructionBlueprint(node.get(1));
		productionBlueprint = new ProductionBlueprint(node.get(2));
	}
}