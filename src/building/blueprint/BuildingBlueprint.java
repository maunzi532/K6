package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.io.*;

public record BuildingBlueprint(String name, ConstructionBlueprint construction,
		ProductionBlueprint production, TransporterBlueprint transporter)
{
	public static BuildingBlueprint create(JrsObject data, ItemLoader itemLoader)
	{
		String name = data.get("Name").asText();
		ConstructionBlueprint construction = ConstructionBlueprint.create((JrsArray) data.get("Construction"), itemLoader);
		ProductionBlueprint production;
		TransporterBlueprint transporter;
		if(data.get("Production") != null)
		{
			production = ProductionBlueprint.create((JrsObject) data.get("Production"), itemLoader);
			transporter = null;
		}
		else if(data.get("Transporter") != null)
		{
			production = null;
			transporter = TransporterBlueprint.create((JrsObject) data.get("Transporter"));
		}
		else
		{
			throw new IllegalArgumentException("BuildingBlueprint must have either \"Production\" or \"Transport\" element");
		}
		return new BuildingBlueprint(name, construction, production, transporter);
	}

	public <T extends ComposerBase> void save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		a1.put("Name", name);
		construction.save(a1.startArrayField("Construction"), itemLoader);
		if(production != null)
		{
			production.save(a1.startObjectField("Production"), itemLoader);
		}
		if(transporter != null)
		{
			transporter.save(a1.startObjectField("Transporter"));
		}
		a1.end();
	}
}