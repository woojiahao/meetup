package database

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


fun getConfiguration(configurationName: Configuration.Name): Configuration? =
  transaction {
    val matchingConfiguration = Configurations.select { Configurations.configurationName eq configurationName.name }

    if (matchingConfiguration.empty()) null
    else matchingConfiguration
      .first()
      .let {
        Configuration(
          it[Configurations.index],
          it[Configurations.configurationName],
          it[Configurations.configurationValue]
        )
      }
  }

fun <T> updateConfiguration(configurationName: Configuration.Name, configurationValue: T) {
  transaction {
    Configurations.update({ Configurations.configurationName eq configurationName.name }) {
      it[Configurations.configurationValue] = configurationValue.toString()
    }
  }
}

fun <T> insertConfiguration(configurationName: Configuration.Name, configurationValue: T) {
  transaction {
    Configurations.insert {
      it[Configurations.configurationName] = configurationName.name
      it[Configurations.configurationValue] = configurationValue.toString()
    }
  }
}

fun <T> addConfiguration(configurationName: Configuration.Name, configurationValue: T) {
  val configuration = getConfiguration(configurationName)
  configuration
    ?.let { updateConfiguration(configurationName, configurationValue) }
    ?: insertConfiguration(configurationName, configurationValue)
}