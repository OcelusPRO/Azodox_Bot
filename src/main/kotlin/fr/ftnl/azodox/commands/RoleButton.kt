package fr.ftnl.azodox.commands

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent

class RoleButton: IButtonCmd {
    override suspend fun action(event: ButtonInteractionEvent) {
        val roleId = event.componentId.split("::")[1]
        if(!event.isFromGuild)
            return event.reply("You can't use this command in a DM").queue()
        val role = event.guild!!.getRoleById(roleId) ?: return event.reply("Role not found").queue()
        if (event.member!!.roles.contains(role)) {
            event.guild!!.removeRoleFromMember(event.user, role).queue()
            event.reply("Le rôle ${role.name} a été retiré").setEphemeral(true).queue()
        }else{
            event.guild!!.addRoleToMember(event.user, role).queue()
            event.reply("Le rôle ${role.name} a été ajouté").setEphemeral(true).queue()
        }
    }

    override val name: String
        get() = "Roles::"
    override val permissions: List<Permission>
        get() = listOf(Permission.MESSAGE_SEND)
}