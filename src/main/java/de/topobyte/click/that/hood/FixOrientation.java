package de.topobyte.click.that.hood;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.locationtech.jts.geom.Geometry;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

public class FixOrientation
{

	public void execute(Path file) throws IOException
	{
		List<Feature> resultFeatures = new ArrayList<>();

		GeoJSONReader reader = new GeoJSONReader();
		GeoJSONWriter writer = new GeoJSONWriter();

		try (InputStream input = Files.newInputStream(file)) {
			String json = IOUtils.toString(input, StandardCharsets.UTF_8);
			FeatureCollection fc = (FeatureCollection) GeoJSONFactory
					.create(json);

			for (Feature feature : fc.getFeatures()) {
				Geometry geometry = reader.read(feature.getGeometry());
				Orientation before = Util.checkOrientation(geometry);
				System.out.println(before);
				geometry.normalize();
				Orientation after = Util.checkOrientation(geometry);
				System.out.println(after);

				Feature newFeature = new Feature(writer.write(geometry),
						feature.getProperties());
				resultFeatures.add(newFeature);
			}
		}

		FeatureCollection result = writer.write(resultFeatures);

		try (OutputStream output = Files.newOutputStream(file)) {
			IOUtils.write(result.toString(), output, StandardCharsets.UTF_8);
			output.write('\n');
		}
	}

}
