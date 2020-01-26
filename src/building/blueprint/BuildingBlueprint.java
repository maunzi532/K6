package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import file.*;
import item.*;
import java.io.*;

public class BuildingBlueprint implements FullBlueprint
{
	public final String name;
	public final ConstructionBlueprint constructionBlueprint;
	public final ProductionBlueprint productionBlueprint;
	public final TransporterBlueprint transporterBlueprint;

	public BuildingBlueprint(String name, ConstructionBlueprint constructionBlueprint,
			ProductionBlueprint productionBlueprint, TransporterBlueprint transporterBlueprint)
	{
		this.name = name;
		this.constructionBlueprint = constructionBlueprint;
		this.productionBlueprint = productionBlueprint;
		this.transporterBlueprint = transporterBlueprint;
	}

	public BuildingBlueprint(JrsObject data, ItemLoader itemLoader)
	{
		name = data.get("Name").asText();
		constructionBlueprint = new ConstructionBlueprint((JrsArray) data.get("Construction"), itemLoader);
		if(data.get("Production") != null)
		{
			productionBlueprint = new ProductionBlueprint((JrsObject) data.get("Production"), itemLoader);
			transporterBlueprint = null;
		}
		else if(data.get("Transporter") != null)
		{
			productionBlueprint = null;
			transporterBlueprint = new TransporterBlueprint((JrsObject) data.get("Transporter"));
		}
		else
			throw new RuntimeException();
	}

	@Override
	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		var a2 = a1.put("Name", name);
		a2 = constructionBlueprint.save(a2.startArrayField("Construction"), itemLoader).end();
		if(productionBlueprint != null)
		{
			a2 = productionBlueprint.save(a2.startObjectField("Production"), itemLoader).end();
		}
		if(transporterBlueprint != null)
		{
			a2 = transporterBlueprint.save(a2.startObjectField("Transporter")).end();
		}
		return a2;
	}
}