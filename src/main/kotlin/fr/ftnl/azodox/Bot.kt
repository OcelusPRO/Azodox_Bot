package fr.ftnl.azodox

import dev.minn.jda.ktx.jdabuilder.injectKTX
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag

class Bot {
    fun start() {
        val builder = DefaultShardManagerBuilder.createDefault(CONFIG.token)
        builder.enableIntents(GatewayIntent.values().toList())
        builder.setMemberCachePolicy(MemberCachePolicy.NONE)
        builder.disableCache(CacheFlag.values().asList())
        builder.addEventListeners(Listener())
        builder.injectKTX()
        builder.setStatus(OnlineStatus.ONLINE)
        builder.build()
    }
}