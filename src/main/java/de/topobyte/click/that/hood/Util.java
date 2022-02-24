package de.topobyte.click.that.hood;

import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

public class Util
{

	public static Orientation checkOrientation(Geometry geometry)
	{
		if (geometry instanceof Polygon) {
			Polygon polygon = (Polygon) geometry;
			LinearRing outer = (LinearRing) polygon.getExteriorRing();
			return orientation(outer.getCoordinateSequence());
		} else if (geometry instanceof MultiPolygon) {
			MultiPolygon mp = (MultiPolygon) geometry;
			Orientation result = null;
			for (int i = 0; i < mp.getNumGeometries(); i++) {
				Polygon polygon = (Polygon) mp.getGeometryN(i);
				LinearRing outer = (LinearRing) polygon.getExteriorRing();
				Orientation orientation = orientation(
						outer.getCoordinateSequence());
				if (result == null) {
					result = orientation;
				} else if (result != orientation) {
					result = Orientation.MIXED;
				}
			}
			return result;
		}
		throw new RuntimeException("Only Polygons supported, found: "
				+ geometry.getClass().getSimpleName());
	}

	private static Orientation orientation(CoordinateSequence sequence)
	{
		if (org.locationtech.jts.algorithm.Orientation.isCCW(sequence)) {
			return Orientation.COUNTER_CLOCKWISE;
		} else {
			return Orientation.CLOCKWISE;
		}
	}

}
