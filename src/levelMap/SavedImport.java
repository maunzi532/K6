package levelMap;

import building.adv.*;
import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.stree.*;
import entity.*;
import item.*;
import item.inv.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;
import javafx.stage.*;

public class SavedImport
{
	private File file;
	private File file2;

	public SavedImport()
	{
		file = new FileChooser().showOpenDialog(null);
		file2 = new FileChooser().showOpenDialog(null);
	}

	public SavedImport(String loadFile, String loadFile2)
	{
		file = new File(loadFile);
		file2 = new File(loadFile2);
	}

	public boolean hasFile()
	{
		return file != null && file2 != null && file.exists() && file2.exists();
	}

	public void importIntoMap3(LevelMap levelMap, CombatSystem combatSystem, ItemLoader itemLoader, Inv storage)
	{
		try
		{
			var tree = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(new String(Files.readAllBytes(file.toPath())));
			var tree2 = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(new String(Files.readAllBytes(file2.toPath())));
			if(((JrsNumber) tree.get("code")).getValue().intValue() == 0xA4D2839F &&
					((JrsNumber) tree2.get("code")).getValue().intValue() == 0xA4D2839F)
			{
				ByteBuffer sb = ByteBuffer.wrap(Base64.getDecoder().decode(((JrsString) tree.get("FloorTiles")).getValue()));
				int lenTiles = sb.remaining() / 4;
				for(int i = 0; i < lenTiles; i++)
				{
					levelMap.createTile(sb.get(), sb.get(), sb.get(), sb.get());
				}
				List<XBuilding> buildings = new ArrayList<>();
				((JrsArray) tree.get("Buildings")).elements().forEachRemaining(e ->
				{
					XBuilding building = new XBuilding((JrsObject) e, itemLoader, levelMap.y1);
					buildings.add(building);
					levelMap.addBuilding(building);
				});
				Map<String, JrsObject> characters = new HashMap<>();
				((JrsArray) tree2.get("Characters")).elements().forEachRemaining(
						character -> characters.put(((JrsObject) ((JrsObject) character).get("Stats")).get("CustomName").asText(), (JrsObject) character));
				((JrsArray) tree.get("XEntities")).elements().forEachRemaining(e ->
						levelMap.addEntity(combatSystem.loadEntityOrStartLoc(levelMap.y1,
								(JrsObject) e, itemLoader, characters, storage)));
				buildings.forEach(levelMap::loadConnectBuilding);
			}
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}