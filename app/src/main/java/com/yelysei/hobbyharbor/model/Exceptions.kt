package com.yelysei.hobbyharbor.model

open class AppException : RuntimeException()

class HobbyDoesNotExistException : AppException()

class UserHobbyDoesNotExistException : AppException()

class NoUserHobbiesFoundException : AppException()

class NoHobbyIdException: AppException()

class InsertUserHobbyException: AppException()

class NoHobbiesBySpecifiedCategoryName: AppException()

class NoActionsByProgressIdException : AppException()