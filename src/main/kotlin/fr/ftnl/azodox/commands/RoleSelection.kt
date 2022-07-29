package fr.ftnl.azodox.commands

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent

class RoleSelection: ISelectCmd {
    override suspend fun action(event: SelectMenuInteractionEvent) {
        if(!event.isFromGuild)
            return event.reply("You can't use this command in a DM").queue()
        val selectedRoles = mutableListOf<Role?>()
        event.selectedOptions.forEach { selectedRoles.add(event.guild!!.getRoleById(it.value)) }
        val allRoles = event.selectMenu.options.mapNotNull { event.guild!!.getRoleById(it.value) }

        val added = mutableListOf<Role>()
        val removed = mutableListOf<Role>()
        allRoles.forEach {
            if (selectedRoles.contains(it)){
                if (!event.member!!.roles.contains(it)){
                    added.add(it)
                    event.guild!!.addRoleToMember(event.user, it).queue()
                }
            }else {
                if (event.member!!.roles.contains(it)){
                    removed.add(it)
                    event.guild!!.removeRoleFromMember(event.user, it).queue()
                }
            }
        }

        val beginning = added.joinToString (" ") { it.name }
        val end = removed.joinToString (" ") { it.name }
        event.reply("""
            Vous avez : 
            ${if(beginning.isNotEmpty()) "> Rôle(s) ajouté(s) : $beginning" else ""}
            ${if(end.isNotEmpty()) "> Rôle(s) retiré(s) : $end" else ""}
        """.trimIndent()).setEphemeral(true).queue()
    }

    override val name: String
        get() = "Roles::"
    override val permissions: List<Permission>
        get() = listOf(Permission.MESSAGE_SEND)
}