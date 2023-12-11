package com.yelysei.hobbyharbor.model.sqlite

object AppSQLiteContract {
    object UserHobbiesTable {
        const val TABLE_NAME = "user_hobbies"
        const val COLUMN_ID = "id"
        const val COLUMN_HOBBY_ID = "hobby_id"
        const val COLUMN_PROGRESS_ID = "progress_id"
    }

    object ProgressesTable {
        const val TABLE_NAME = "progresses"
        const val COLUMN_ID = "id"
        const val COLUMN_GOAL = "goal"
    }

    object HobbiesTable {
        const val TABLE_NAME = "hobbies"
        const val COLUMN_ID = "id"
        const val COLUMN_HOBBY_NAME = "hobby_name"
        const val COLUMN_CATEGORY_NAME = "category_name"
        const val COLUMN_COST = "cost"
        const val COLUMN_PLACE = "place"
        const val COLUMN_PEOPLE = "people"
    }

    object ActionsTable {
        const val TABLE_NAME = "actions"
        const val COLUMN_ID = "id"
        const val COLUMN_START_DATE = "start_date"
        const val COLUMN_END_DATE = "end_date"
        const val COLUMN_PROGRESS_ID = "progress_id`"
    }
}