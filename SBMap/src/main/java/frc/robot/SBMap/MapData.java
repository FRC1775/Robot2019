package frc.robot.SBMap;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;
import edu.wpi.first.shuffleboard.api.util.Maps;

import frc.robot.SBMap.Point2D;

import java.util.Map;
import java.util.function.Function;

/**
 * Represents data of the {@link Point2D} type.
 */
public final class MapData extends ComplexDataType<Point2D> {

  /**
   * The name of data of this type as it would appear in a WPILib sendable's {@code .type} entry; a differential drive
   * base a {@code .type} of "DifferentialDrive", a sendable chooser has it set to "String Chooser"; a hypothetical
   * 2D point would have it set to "Point2D".
   */
  private static final String TYPE_NAME = "Point2D";

  /**
   * The single instance of the point type. By convention, this is a {@code public static final}
   * field and the constructor is private to ensure only a single instance of the data type exists.
   */
  public static final MapData Instance = new MapData();

  private MapData() {
    super(TYPE_NAME, Point2D.class);
  }

  @Override
  public Function<Map<String, Object>, Point2D> fromMap() {
    return map -> new Point2D(
        Maps.getOrDefault(map, "x", 0),
        Maps.getOrDefault(map, "y", 0)
    );
  }

  @Override
  public Point2D getDefaultValue() {
    return new Point2D(0, 0);
  }
}