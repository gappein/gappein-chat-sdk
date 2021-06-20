package com.gappein.coroutine.sdk.service

import com.gappein.coroutine.sdk.service.channel.ChannelService
import com.gappein.coroutine.sdk.service.channel.ChannelServiceImpl
import com.gappein.coroutine.sdk.service.user.UserService

/**
 * Created by Himanshu Singh on 21-02-2021.
 * hello2himanshusingh@gmail.com
 */
data class GappeinDbService(

    val userService: UserService = UserServiceImpl(),

    val channelService: ChannelService = ChannelServiceImpl()

)