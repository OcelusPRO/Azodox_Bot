package fr.ftnl.azodox.commands

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.DiscordLocale
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.components.buttons.Button

class PingCmd: ISlashCmd {
    override val description: String
        get() = "pong"
    override val options: List<OptionData>
        get() = listOf()

    override suspend fun action(event: SlashCommandInteractionEvent) {
        event.reply("Pong").addActionRow(
            Button.primary("Test::", "Pong").withEmoji(Emoji.fromUnicode("\uD83D\uDE0A"))
        ).queue()
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