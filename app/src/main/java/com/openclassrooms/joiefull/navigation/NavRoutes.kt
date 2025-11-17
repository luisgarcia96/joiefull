package com.openclassrooms.joiefull.navigation

object NavRoutes {
  const val HOME = "home"
  const val DETAILS = "details"
  const val DETAILS_ARG_ID = "id"

  fun detailsRoute(itemId: String) = "$DETAILS/$itemId"
}
