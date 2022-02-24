package de.topobyte.click.that.hood;

import java.io.IOException;
import java.nio.file.Path;

import de.topobyte.system.utils.SystemPaths;

public class FixBadBelzig
{

	public static void main(String[] args) throws IOException
	{
		Path file = SystemPaths.HOME.resolve(
				"github/codeforgermany/click_that_hood/public/data/bad-belzig.geojson");
		new FixOrientation().execute(file);
	}

}
