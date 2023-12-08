package com.yelysei.hobbyharbor.app.views.userhobbies

//class UserHobbiesViewModel(
//    private val navigator: Navigator,
//    private val hobbiesRepository: UserHobbiesRepository
//) : BaseViewModel(){
//
//    private val _userHobbies = MutableLiveData<List<UserHobby>>()
//    val userHobbies: LiveData<List<UserHobby>> = _userHobbies
//
//    private val listener: UserHobbiesListener = {
//        _userHobbies.value = it
//    }
//
//    init {
//        loadUserHobbies()
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        hobbiesRepository.removeListener(listener)
//    }
//
//    private fun loadUserHobbies() {
//        hobbiesRepository.addListener(listener)
//    }
//
//    fun onUserHobbyPressed(uhId: Long) {
//        navigator.launch(UserHobbyDetailsFragment.Screen(uhId))
//    }
//
//    fun onAddPressed() {
//        navigator.launch(CategoriesFragment.Screen())
//    }

//}