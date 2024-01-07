package com.yelysei.hobbyharbor.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yelysei.hobbyharbor.model.hobbies.room.HobbiesDao
import com.yelysei.hobbyharbor.model.hobbies.room.entities.HobbyDbEntity
import com.yelysei.hobbyharbor.model.userhobbies.room.UserHobbiesDao
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.ExperienceDbEntity
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.ImageReferenceDbEntity
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.ProgressDbEntity
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.UserHobbyDbEntity

@Database(
    version = 1,
    entities = [
        HobbyDbEntity::class,
        UserHobbyDbEntity::class,
        ExperienceDbEntity::class,
        ProgressDbEntity::class,
        ImageReferenceDbEntity::class
    ],
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getHobbiesDao(): HobbiesDao

    abstract fun getUserHobbiesDao(): UserHobbiesDao

    companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add or modify tables or columns here
                db.execSQL("ALTER TABLE actions RENAME TO experiences;")
                db.execSQL("ALTER TABLE experiences ADD note TEXT")
                db.execSQL("CREATE TABLE image_references (\n" +
                        "\t\"id\"\tINTEGER NOT NULL,\n" +
                        "\t\"uri_reference\"\tTEXT NOT NULL,\n" +
                        "\t\"experience_id\"\tINTEGER NOT NULL,\n" +
                        "\tFOREIGN KEY(\"experience_id\") REFERENCES \"experiences\"(\"id\") ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                        ");")
            }
        }
    }

}