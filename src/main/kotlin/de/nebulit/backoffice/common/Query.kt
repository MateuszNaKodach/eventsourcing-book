package de.nebulit.backoffice.common

interface Query

interface QueryHandler<T : Query, U> {
  fun handleQuery(query: T): U
}
