package fr.ftnl.azodox

import dev.minn.jda.ktx.events.CoroutineEventListener
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

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
        }


    }
}