package fr.ftnl.azodox

import dev.minn.jda.ktx.events.CoroutineEventListener
import fr.ftnl.azodox.commands.IButtonCmd
import fr.ftnl.azodox.commands.ICmd
import fr.ftnl.azodox.commands.ISelectCmd
import fr.ftnl.azodox.commands.ISlashCmd
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent

class Listener: CoroutineEventListener {
    override suspend fun onEvent(event: GenericEvent) {

        when (event) {
            is ReadyEvent -> {
                ICmd.postDataCmd(event.jda)
                println(event.jda.selfUser.asTag + " is ready! " + event.jda.selfUser.id)
                println(event.jda.getInviteUrl(Permission.ADMINISTRATOR))
            }
            is SlashCommandInteractionEvent -> {
                val eventName = event.name
                val cmd = ICmd.cmd.filterIsInstance<ISlashCmd>().find { it.name == eventName } ?: return
                cmd.action(event)
            }
            is ButtonInteractionEvent -> {
                val eventName = event.componentId
                val cmd = ICmd.cmd.filterIsInstance<IButtonCmd>().find { eventName.startsWith(it.name) } ?: return
                if(event.isFromGuild)
                    if (!cmd.permissions.all { permission  ->  event.member?.hasPermission(permission) == true })
                        return
                cmd.action(event)
            }
            is SelectMenuInteractionEvent -> {
                val eventName = event.componentId
                val cmd = ICmd.cmd.filterIsInstance<ISelectCmd>().find { eventName.startsWith(it.name) } ?: return
                if(event.isFromGuild)
                    if (!cmd.permissions.all { permission  ->  event.member?.hasPermission(permission) == true })
                        return
                cmd.action(event)
            }
        }


    }
}