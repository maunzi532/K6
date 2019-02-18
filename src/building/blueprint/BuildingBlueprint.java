package building.blueprint;

import file.*;

public class BuildingBlueprint implements FullBlueprint
{
	public final String name;
	public final ConstructionBlueprint constructionBlueprint;
	public final int type;
	public ProductionBlueprint productionBlueprint;
	public TransporterBlueprint transporterBlueprint;

	public BuildingBlueprint(String name, ConstructionBlueprint constructionBlueprint,
			ProductionBlueprint productionBlueprint)
	{
		this.name = name;
		this.constructionBlueprint = constructionBlueprint;
		this.productionBlueprint = productionBlueprint;
		type = 0;
	}

	public BuildingBlueprint(String name, ConstructionBlueprint constructionBlueprint,
			TransporterBlueprint transporterBlueprint)
	{
		this.name = name;
		this.constructionBlueprint = constructionBlueprint;
		this.transporterBlueprint = transporterBlueprint;
		type = 1;
	}

	public BuildingBlueprint(BlueprintNode node)
	{
		if(!node.data.equals(getClass().getSimpleName()))
			throw new RuntimeException(node.data + ", required: " + getClass().getSimpleName());
		name = node.get(0).data;
		constructionBlueprint = new ConstructionBlueprint(node.get(1));
		String n2Name = node.get(2).data;
		if("ProductionBlueprint".equals(n2Name))
		{
			type = 0;
			productionBlueprint = new ProductionBlueprint(node.get(2));
		}
		else if("TransporterBlueprint".equals(n2Name))
		{
			type = 1;
			transporterBlueprint = new TransporterBlueprint(node.get(2));
		}
		else
		{
			throw new RuntimeException(n2Name + ", must be building type");
		}
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

	@Override
	public String name()
	{
		return name;
	}
}