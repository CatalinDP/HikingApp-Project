package database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import dao.Converters;
import dao.DaoPointOfInterest;
import dao.DaoRoute;
import models.PointOfInterest;
import models.Route;

/*
* Room obliga a incrementar la versión cada vez que:

Añades una nueva entidad (@Entity)
Cambias el esquema de una entidad existente (añades/eliminas campos, cambias tipos, añades índices, foreign keys, etc.)
Modificas cualquier cosa que afecte la estructura de las tablas
(para lo de la versión 2)(para que se entienda el por qué, si no se cambia crashea -experiencia propia-)
* */

@Database(entities = {Route.class, PointOfInterest.class}, version = 4, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract DaoRoute daoRoute();
    public abstract DaoPointOfInterest daoPointOfInterest();

    private static AppDatabase appDatabase;

    public static AppDatabase getAppDatabase(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "RoutesDB")
                    .fallbackToDestructiveMigration(true) // .fallbackToDestructiveMigration(true) → destruye y recrea la BD si el esquema cambia (temporal mi bro)
                    .build();
        }
        return appDatabase;
    }
}