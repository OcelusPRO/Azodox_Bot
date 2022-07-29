package fr.ftnl.azodox.commands

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.interactions.components.SelectOption
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.DiscordLocale
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu

class RoleCmd: ISlashCmd {
    override val description: String
        get() = "Choisir un rôle"
    override val options: List<OptionData>
        get() = listOf(
            OptionData(OptionType.ROLE, "role", "Rôle à choisir", true),
            OptionData(OptionType.STRING, "message", "ID du message", false),
            OptionData(OptionType.STRING, "content", "Quel message sera affiché (uniquement pour le 1er rôle)", false),
        )

    override suspend fun action(event: SlashCommandInteractionEvent) {
        val role = event.getOptionsByName("role").first().asRole
        val message = event.getOptionsByName("message").firstOrNull()?.asString
        val content = event.getOptionsByName("content").firstOrNull()?.asString

        if(message == null){
            event.reply(content ?: "Sélectionnez vos rôles :").addActionRow(
                SelectMenu.create("Roles::")
                    .addOptions(SelectOption(role.name, role.id))
                    .setMinValues(0)
                    .build()
            ).queue()
        }else{
            val channel = event.channel.asTextChannel()
            val msg = channel.retrieveMessageById(message).await()
            val currentRows = msg.actionRows
            val selectMenu = currentRows[0].components[0] as? SelectMenu ?: return event.reply("Ce message n'est pas un message de sélection de rôle").queue()
            val newSelectMenu = SelectMenu.create(selectMenu.id!!)
                .addOptions(selectMenu.options)
                .addOption(role.name, role.id)
                .setMaxValues(selectMenu.options.size + 1)
                .setMinValues(0)
                .build()

            msg.editMessageComponents(ActionRow.of(newSelectMenu)).queue()
            event.reply("Rôle ajouté").setEphemeral(true).queue()
        }
    }

    override val localizedNames: Map<DiscordLocale, String>
        get() = mapOf(
            DiscordLocale.FRENCH to "ajouter-auto-role",
            DiscordLocale.ENGLISH_UK to "role",
            DiscordLocale.ENGLISH_US to "role"
        )
    override val name: String
        get() = "add-auto-role"
    override val permissions: List<Permission>
        get() = listOf(Permission.MANAGE_ROLES)
}