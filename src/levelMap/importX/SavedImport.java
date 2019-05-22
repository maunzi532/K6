package levelMap.importX;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import javafx.stage.*;
import logic.*;

public class SavedImport
{
	private File file;

	public SavedImport()
	{
		file = new FileChooser().showOpenDialog(null);
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
}