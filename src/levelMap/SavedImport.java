package levelMap;

import building.adv.*;
import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.stree.*;
import entity.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;
import javafx.stage.*;
import system2.*;
import system2.analysis.*;

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

	public void importIntoMap3(LevelMap levelMap, ItemLoader itemLoader, Inv storage)
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
						levelMap.addEntity(loadCharacterOrStartLoc(levelMap,
								(JrsObject) e, itemLoader, characters, storage)));
				buildings.forEach(levelMap::loadConnectBuilding);
			}
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public XCharacter loadCharacterOrStartLoc(LevelMap levelMap,
			JrsObject data, ItemLoader itemLoader, Map<String, JrsObject> characters, Inv storage)
	{
		Tile location = levelMap.y1.create2(((JrsNumber) data.get("sx")).getValue().intValue(), ((JrsNumber) data.get("sy")).getValue().intValue());
		if(data.get("StartName") != null)
		{
			int classCode = 1;
			String startName = data.get("StartName").asText();
			boolean locked = ((JrsBoolean) data.get("Locked")).booleanValue();
			boolean invLocked = ((JrsBoolean) data.get("InvLocked")).booleanValue();
			JrsObject char1 = characters.get(startName);
			Stats stats = new Stats(((JrsObject) char1.get("Stats")), itemLoader);
			Inv inv;
			if(invLocked)
			{
				inv = new WeightInv(((JrsObject) data.get("Inventory")), itemLoader);
				Inv inv2 = new WeightInv(((JrsObject) char1.get("Inventory")), itemLoader);
				storage.tryAdd(inv2.allItems(), false, CommitType.COMMIT);
			}
			else
			{
				inv = new WeightInv(((JrsObject) char1.get("Inventory")), itemLoader);
			}
			return switch(classCode)
					{
						case 1 -> new XCharacter(CharacterTeam.HERO, 0, location, stats, inv,
								null, null, new SaveSettings(locked, invLocked));
						case 2 -> new XCharacter(CharacterTeam.ENEMY, 0, location, stats, inv,
								null, null, new SaveSettings(locked, invLocked));
						default -> throw new RuntimeException();
					};
		}
		else
		{
			int classCode = ((JrsNumber) data.get("Type")).getValue().intValue();
			Stats stats = new Stats(((JrsObject) data.get("Stats")), itemLoader);
			Inv inv = new WeightInv(((JrsObject) data.get("Inventory")), itemLoader);
			return switch(classCode)
					{
						case 1 -> new XCharacter(CharacterTeam.HERO, 0, location, stats, inv,
								null, null, null);
						case 2 -> new XCharacter(CharacterTeam.ENEMY, 0, location, stats, inv,
								new StandardAI(levelMap), null, null);
						default -> throw new RuntimeException();
					};
		}
	}
}