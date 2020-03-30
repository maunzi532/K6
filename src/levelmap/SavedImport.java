package levelmap;

import building.adv.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.stree.*;
import entity.*;
import geom.tile.*;
import item.*;
import item.inv.*;
import java.io.*;
import java.nio.*;
import java.util.*;
import statsystem.*;
import statsystem.analysis.*;

public final class SavedImport
{
	private final String inputMap;
	private final String inputTeam;

	public SavedImport(String inputMap, String inputTeam)
	{
		this.inputMap = inputMap;
		this.inputTeam = inputTeam;
	}

	public void importIntoMap3(LevelMap levelMap, ItemLoader itemLoader, Inv storage)
	{
		try
		{
			TreeNode tree = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(inputMap);
			TreeNode treeXh = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(inputTeam);
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
				storage.tryAdd(storageLoad.allItems());
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

	private XCharacter loadCharacterOrStartLoc(LevelMap levelMap,
			JrsObject data, ItemLoader itemLoader, Map<String, ? extends JrsObject> characters, Inv storage)
	{
		Tile location = levelMap.y1.create2(((JrsNumber) data.get("sx")).getValue().intValue(), ((JrsNumber) data.get("sy")).getValue().intValue());
		CharacterTeam team = CharacterTeam.valueOf(data.get("Type").asText());
		int startingDelay = ((JrsNumber) data.get("StartingDelay")).getValue().intValue();
		Stats stats;
		Inv inv;
		StartingSettings startingSettings;
		if(data.get("StartName") != null)
		{
			boolean locked = ((JrsBoolean) data.get("Locked")).booleanValue();
			boolean invLocked = ((JrsBoolean) data.get("InvLocked")).booleanValue();
			startingSettings = new StartingSettings(locked, invLocked);
			JrsObject char1 = characters.get(data.get("StartName").asText());
			stats = new Stats(((JrsObject) char1.get("Stats")), itemLoader);
			if(invLocked)
			{
				inv = new WeightInv(((JrsObject) data.get("Inventory")), itemLoader);
				Inv inv2 = new WeightInv(((JrsObject) char1.get("Inventory")), itemLoader);
				storage.tryAdd(inv2.allItems());
			}
			else
			{
				inv = new WeightInv(((JrsObject) char1.get("Inventory")), itemLoader);
			}
		}
		else
		{
			stats = new Stats(((JrsObject) data.get("Stats")), itemLoader);
			inv = new WeightInv(((JrsObject) data.get("Inventory")), itemLoader);
			startingSettings = null;
		}
		EnemyAI enemyAI = team == CharacterTeam.ENEMY ? new StandardAI(levelMap) : new NoAI();
		return new XCharacter(team, startingDelay, location, stats, inv, enemyAI, new TurnResources(location), startingSettings);
	}
}