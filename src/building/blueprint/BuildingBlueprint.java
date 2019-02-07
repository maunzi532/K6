package building.blueprint;

import file.*;

public class BuildingBlueprint implements FullBlueprint
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

	public BuildingBlueprint(BlueprintNode node)
	{
		if(!node.data.equals(getClass().getSimpleName()))
			throw new RuntimeException(node.data + ", required: " + getClass().getSimpleName());
		name = node.get(0).data;
		constructionBlueprint = new ConstructionBlueprint(node.get(1));
		productionBlueprint = new ProductionBlueprint(node.get(2));
	}

	public static BuildingBlueprint get(BlueprintCache<BuildingBlueprint> cache, String name)
	{
		BuildingBlueprint b1 = cache.getCached(name);
		if(b1 != null)
			return b1;
		BuildingBlueprint b2 = new BuildingBlueprint(cache.getInput(name));
		cache.putCached(name, b2);
		return b2;
	}
}