package com.example.mad_collaborative

data class EventModel(
    var date: String = "",
    var time: String = "",
    var location: String = "",
    var imageUrl: String? = null,
    var isCreated: Boolean = false,
    var id: String? = null,
    var isJoined: Boolean = false,
    var participants: List<String> = mutableListOf(),
    var eventName: String = "",
)
