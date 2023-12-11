package com.yelysei.hobbyharbor.model

open class AppException : RuntimeException()

class HobbyDoesNotExistException : AppException()

class UserHobbyDoesNotExistException : AppException()

class NoUserHobbiesFoundException : AppException()