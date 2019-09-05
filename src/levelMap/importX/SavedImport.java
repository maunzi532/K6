package levelMap.importX;

import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;
import javafx.stage.*;
import logic.*;

public class SavedImport
{
	private File file;

	public SavedImport()
	{
		file = new FileChooser().showOpenDialog(null);
	}

	public SavedImport(String loadFile)
	{
		file = new File(loadFile);
	}

	public boolean hasFile()
	{
		return file != null && file.exists();
	}

	public void importIntoMap(MainState mainState)
	{
		try
		{
			byte[] bytes = Files.readAllBytes(file.toPath());
			ByteBuffer sb = ByteBuffer.wrap(bytes);
			if(sb.getInt() != 0xA4D2839F)
				throw new RuntimeException("Wrong file");
			int lenTiles = sb.getInt();
			for(int i = 0; i < lenTiles; i++)
			{
				mainState.levelMap.createTile(sb.get(), sb.get(), sb.get(), sb.get());
			}
			int lenEntities = sb.getInt();
			IntBuffer sb3 = sb.asIntBuffer();
			for(int i = 0; i < lenEntities; i++)
			{
				mainState.levelMap.addEntity(mainState.combatSystem.loadEntity(mainState.y2, mainState, sb3));
			}
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void importIntoMap2(MainState mainState)
	{
		try
		{
			var tree = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(new String(Files.readAllBytes(file.toPath())));
			if(((JrsNumber) tree.get("code")).getValue().intValue() == 0xA4D2839F)
			{
				ByteBuffer sb = ByteBuffer.wrap(Base64.getDecoder().decode(((JrsString) tree.get("FloorTiles")).getValue()));
				int lenTiles = sb.remaining() / 4;
				for(int i = 0; i < lenTiles; i++)
				{
					mainState.levelMap.createTile(sb.get(), sb.get(), sb.get(), sb.get());
				}
				((JrsArray) tree.get("XEntities")).elements().forEachRemaining(e ->
						mainState.levelMap.addEntity(mainState.combatSystem.loadEntity(mainState.y2, mainState, (JrsObject) e)));
			}
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}