package com.yelysei.hobbyharbor.model

open class AppException : RuntimeException()

class HobbyDoesNotExistException : AppException()

class UserHobbyDoesNotExistException : AppException()

class NoUserHobbiesFoundException : AppException()

class NoHobbyIdException: AppException()

class InsertUserHobbyException: AppException()

class NoHobbiesBySpecifiedCategoryName: AppException()

class NoActionsByProgressIdException : AppException()

class AddHobbyNameAlreadyEgsistsException : AppException()

class AddHobbyStartingProgressGreaterThanDesiredException : AppException()

class AddHobbyStartingProgressLowerThanDesiredException : AppException()

/*
Exepctions handled by input:
 - new hobby (empty name, to long name, name with illegal chars )
 - add time to hobby (time below 0, wrong date, starting date is after ending date)
 - add progress to hobby (below 0, bigger than desired final progress, desired hobby progress to big)
 */