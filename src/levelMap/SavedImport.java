package levelMap;

import building.adv.*;
import com.fasterxml.jackson.core.*;
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
	private File fileXh;

	public SavedImport()
	{
		file = new FileChooser().showOpenDialog(null);
		fileXh = new FileChooser().showOpenDialog(null);
	}

	public SavedImport(String loadFile, String loadFileXh)
	{
		file = new File(loadFile);
		fileXh = new File(loadFileXh);
	}

	public boolean hasFile()
	{
		return file != null && fileXh != null && file.exists() && fileXh.exists();
	}

	public void importIntoMap3(LevelMap levelMap, ItemLoader itemLoader, Inv storage)
	{
		try
		{
			TreeNode tree = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(new String(Files.readAllBytes(file.toPath())));
			TreeNode treeXh = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(new String(Files.readAllBytes(fileXh.toPath())));
			if(((JrsNumber) tree.get("code")).getValue().intValue() == 0xA4D2839F &&
					((JrsNumber) treeXh.get("code")).getValue().intValue() == 0xA4D2839F)
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
				Inv storageLoad = new WeightInv((JrsObject) treeXh.get("Storage"), itemLoader);
				storage.tryAdd(storageLoad.allItems(), false, CommitType.COMMIT);
				Map<String, JrsObject> characters = new HashMap<>();
				((JrsArray) treeXh.get("Characters")).elements().forEachRemaining(
						character -> characters.put(((JrsObject) ((JrsObject) character).get("Stats")).get("CustomName").asText(), (JrsObject) character));
				((JrsArray) tree.get("Characters")).elements().forEachRemaining(e ->
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
		CharacterTeam team;
		Stats stats;
		Inv inv;
		SaveSettings saveSettings;
		if(data.get("StartName") != null)
		{
			team = data.get("Type") != null ? CharacterTeam.valueOf(data.get("Type").asText()) : CharacterTeam.HERO;
			String startName = data.get("StartName").asText();
			boolean locked = ((JrsBoolean) data.get("Locked")).booleanValue();
			boolean invLocked = ((JrsBoolean) data.get("InvLocked")).booleanValue();
			saveSettings = new SaveSettings(locked, invLocked);
			JrsObject char1 = characters.get(startName);
			stats = new Stats(((JrsObject) char1.get("Stats")), itemLoader);
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
		}
		else
		{
			team = CharacterTeam.valueOf(data.get("Type").asText());
			stats = new Stats(((JrsObject) data.get("Stats")), itemLoader);
			inv = new WeightInv(((JrsObject) data.get("Inventory")), itemLoader);
			saveSettings = null;
		}
		EnemyAI enemyAI = team == CharacterTeam.ENEMY ? new StandardAI(levelMap) : null;
		return new XCharacter(team, 0, location, stats, inv, enemyAI, null, saveSettings);
	}
}