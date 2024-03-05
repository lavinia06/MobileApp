package com.example.exam

import com.example.exam.model.Car

object Constants {
    const val SERVER_IP = "172.30.114.146"
    const val SERVER_PORT = "2406"

    const val NAVIGATION_LIST = "list"
    const val NAVIGATION_CREATE = "create"
    const val NAVIGATION_EDIT = "edit/{id}"
    const val NAVIGATION_DETAILS = "details/{id}"
    const val NAVIGATION_ARGUMENT = "id"
    const val NAVIGATION_HOME = "home"
    const val NAVIGATION_ORGANIZER = "organizer"
    const val NAVIGATION_STAFF = "staff"
    const val NAVIGATION_SUPPLIER = "supplier"


    fun detailNavigation(id: Int) = "details/$id"
    fun editNavigation(id: Int) = "edit/$id"

    val ENTITY_PLACE_HOLDER = Car(
        id = -1,
        name = "",
        supplier = "",
        details = "",
        status = "",
        quantity = -1,
        type = ""
    )
}