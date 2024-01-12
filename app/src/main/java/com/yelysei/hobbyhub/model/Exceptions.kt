package com.yelysei.hobbyhub.model

open class AppException : RuntimeException()

class NoUserHobbiesFoundException : AppException()

class NoHobbyIdException : AppException()

class UserHobbyAlreadyAddedException : AppException()

class NoHobbiesBySpecifiedCategoryName : AppException()

class NoExperiencesByProgressIdException : AppException()

class HobbyAlreadyExistsException(
    val hobbyName: String
) : AppException()


class UserHobbyIsNotLoadedException : AppException()

class SetGoalIsLessThanAchievedProgress : AppException()


/*
Exepctions handled by input:
 - new hobby (empty name, to long name, name with illegal chars )
 - add time to hobby (time below 0, wrong date, starting date is after ending date)
 - add progress to hobby (below 0, bigger than desired final progress, desired hobby progress to big)
 */