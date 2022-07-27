package fr.ftnl.azodox

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.DiscordLocale
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class PingCmd: ISlashCmd {
    override val description: String
        get() = "pong"
    override val options: List<OptionData>
        get() = listOf()

    override suspend fun action(event: SlashCommandInteractionEvent) {
        event.reply("Pong").queue()
    }

    override val name: String
        get() = "ping"
    override val localizedNames: Map<DiscordLocale, String>
        get() = mapOf(
            DiscordLocale.FRENCH to "ping",
            DiscordLocale.ENGLISH_UK to "ping",
            DiscordLocale.ENGLISH_US to "ping"
        )
    override val permissions: List<Permission>
        get() = listOf(
            Permission.MESSAGE_SEND
        )
}