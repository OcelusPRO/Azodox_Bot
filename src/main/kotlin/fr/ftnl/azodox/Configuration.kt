package fr.ftnl.azodox

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

val GSON: Gson by lazy { GsonBuilder().setPrettyPrinting().serializeNulls().create() }
val CONFIG: Configuration by lazy { Configuration.loadConfiguration(File("./config.json")) }

data class Configuration(
    val token: String = "",
) {
    companion object {
        class ConfigurationException(message: String) : Exception(message)

        fun loadConfiguration(file: File): Configuration {
            if (file.createNewFile()) {
                val config = Configuration()
                file.writeText(GSON.toJson(config))
                throw ConfigurationException("Veuillez remplir le fichier de configuration")
            }
            return try {
                val cfg = GSON.fromJson(file.readText(), Configuration::class.java)
                file.writeText(GSON.toJson(cfg))
                cfg
            } catch (e: Exception) {
                throw ConfigurationException("La configuration n'est pas valide")
            }
        }
    }
}
