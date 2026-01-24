package dao;

import androidx.room.TypeConverter;

import models.Difficulty;
import models.RouteType;

public class Converters {
    @TypeConverter
    public static String fromDifficulty(Difficulty difficulty) {
        return difficulty.toString();
    }

    @TypeConverter
    public static Difficulty toDifficulty(String value) {
        return Difficulty.valueOf(value);
    }

    @TypeConverter
    public static String fromRouteType(RouteType routeType) {
        return routeType.toString();
    }
    @TypeConverter
    public static RouteType toRouteType(String value) {
        return RouteType.valueOf(value);
    }

}
