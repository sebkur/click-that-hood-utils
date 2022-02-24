package de.topobyte.click.that.hood;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygonal;
import org.locationtech.jts.geom.Puntal;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.jts2geojson.GeoJSONReader;

public class CheckOrientation
{

	public void execute(Path file, boolean printDetails) throws IOException
	{
		if (printDetails) {
			System.out.println(file);
		}

		int numBugs = 0;

		GeoJSONReader reader = new GeoJSONReader();

		try (InputStream input = Files.newInputStream(file)) {
			String json = IOUtils.toString(input, StandardCharsets.UTF_8);
			FeatureCollection fc = (FeatureCollection) GeoJSONFactory
					.create(json);

			if (printDetails) {
				System.out.println(
						"Number of features: " + fc.getFeatures().length);
			}

			for (Feature feature : fc.getFeatures()) {
				Geometry geometry = reader.read(feature.getGeometry());
				if (geometry instanceof Puntal) {
					continue;
				}
				if (!(geometry instanceof Polygonal)) {
					System.out.println(
							"Skipping: " + geometry.getClass().getSimpleName());
					continue;
				}
				if (Util.checkOrientation(geometry) != Orientation.CLOCKWISE) {
					Map<String, Object> properties = feature.getProperties();
					if (printDetails) {
						System.out.println("Not clockwise: " + properties);
					}
					numBugs += 1;
				}
			}
		}

		if (numBugs > 0) {
			if (printDetails) {
				System.out.println("Number of bugs: " + numBugs);
			}
			System.out.println(file);
		}
	}

}
