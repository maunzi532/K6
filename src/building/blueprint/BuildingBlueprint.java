package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import file.*;
import item.*;
import java.io.*;

public record BuildingBlueprint(String name, ConstructionBlueprint constructionBlueprint,
		ProductionBlueprint productionBlueprint, TransporterBlueprint transporterBlueprint) implements FullBlueprint
{
	public static BuildingBlueprint create(JrsObject data, ItemLoader itemLoader)
	{
		String name = data.get("Name").asText();
		ConstructionBlueprint constructionBlueprint = ConstructionBlueprint.create((JrsArray) data.get("Construction"), itemLoader);
		ProductionBlueprint productionBlueprint;
		TransporterBlueprint transporterBlueprint;
		if(data.get("Production") != null)
		{
			productionBlueprint = ProductionBlueprint.create((JrsObject) data.get("Production"), itemLoader);
			transporterBlueprint = null;
		}
		else if(data.get("Transporter") != null)
		{
			productionBlueprint = null;
			transporterBlueprint = TransporterBlueprint.create((JrsObject) data.get("Transporter"));
		}
		else
		{
			throw new RuntimeException();
		}
		return new BuildingBlueprint(name, constructionBlueprint, productionBlueprint, transporterBlueprint);
	}

	@Override
	public <T extends ComposerBase> void save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		a1.put("Name", name);
		constructionBlueprint.save(a1.startArrayField("Construction"), itemLoader);
		if(productionBlueprint != null)
		{
			productionBlueprint.save(a1.startObjectField("Production"), itemLoader);
		}
		if(transporterBlueprint != null)
		{
			transporterBlueprint.save(a1.startObjectField("Transporter"));
		}
		a1.end();
	}
}