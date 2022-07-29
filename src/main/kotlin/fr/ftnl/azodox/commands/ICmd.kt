package fr.ftnl.azodox.commands

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent
import net.dv8tion.jda.api.interactions.DiscordLocale
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.components.Modal
import org.reflections.Reflections

interface ICmd {
    val name : String
    val permissions: List<Permission>

    companion object {
        val cmd = getCommands()
        private var posted = false

        private fun getCommands(): List<ICmd> {
            println("Loading commands...")
            val reflections: Set<Class<out ICmd>> =
                Reflections("fr").getSubTypesOf(ICmd::class.java)
            return reflections.filter { !it.isInterface }.map { it.getConstructor().newInstance() }
        }

        fun postDataCmd(jda: JDA) {
            if (posted) return
            val commands = cmd.filterIsInstance(IDataCmd::class.java)
            jda.updateCommands().addCommands(commands.map { it.data }).queue()
            posted = true

            val postedCmds = jda.retrieveCommands().complete()
            postedCmds.forEach {
                println(it)
            }
        }
    }
}

interface IDataCmd: ICmd {
    val data: CommandData
    val localizedNames: Map<DiscordLocale, String>
}

interface ISlashCmd: IDataCmd {
    val description: String
    val options: List<OptionData>

    override val data: CommandData
        get() = Commands.slash(name, description)
            .setNameLocalizations(localizedNames)
            .addOptions(options)
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(permissions))

    suspend fun action(event: SlashCommandInteractionEvent)
    suspend fun action(event: CommandAutoCompleteInteractionEvent) = Unit
}

interface IButtonCmd : ICmd {
    suspend fun action(event: ButtonInteractionEvent)
}

interface IModalCmd : ICmd {
    val modal: Modal
    suspend fun action(event: ModalInteractionEvent)
}

interface ISelectCmd : ICmd {
    suspend fun action(event: SelectMenuInteractionEvent)
}