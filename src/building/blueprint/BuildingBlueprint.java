package building.blueprint;

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
}