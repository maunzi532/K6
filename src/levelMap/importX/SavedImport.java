package levelMap.importX;

import building.adv.*;
import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.stree.*;
import item.inv.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;
import javafx.stage.*;
import logic.*;

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

	public void importIntoMap3(MainState mainState)
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
					mainState.levelMap.createTile(sb.get(), sb.get(), sb.get(), sb.get());
				}
				List<XBuilding> buildings = new ArrayList<>();
				((JrsArray) tree.get("Buildings")).elements().forEachRemaining(e ->
				{
					XBuilding building = new XBuilding((JrsObject) e, mainState.itemLoader, mainState.y1);
					buildings.add(building);
					mainState.levelMap.addBuilding(building);
				});
				Map<String, JrsObject> characters = new HashMap<>();
				((JrsArray) tree2.get("Characters")).elements().forEachRemaining(
						character -> characters.put(((JrsObject) ((JrsObject) character).get("Stats")).get("CustomName").asText(), (JrsObject) character));
				((JrsArray) tree.get("XEntities")).elements().forEachRemaining(e ->
						mainState.levelMap.addEntity(mainState.combatSystem.loadEntityOrStartLoc(mainState.y1, mainState,
								(JrsObject) e, mainState.itemLoader, characters, new WeightInv(100)))); // TODO
				buildings.forEach(e -> e.loadConnect(mainState.levelMap));
			}
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}