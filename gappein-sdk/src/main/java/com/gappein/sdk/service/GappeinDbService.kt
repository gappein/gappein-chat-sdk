package com.gappein.sdk.service

import com.gappein.sdk.service.channel.ChannelService
import com.gappein.sdk.service.channel.ChannelServiceImpl
import com.gappein.sdk.service.user.UserService
import com.gappein.sdk.service.user.UserServiceImpl

/**
 * Created by Himanshu Singh on 21-02-2021.
 * hello2himanshusingh@gmail.com
 */
data class GappeinDbService(

    val userService: UserService = UserServiceImpl(),

    val channelService: ChannelService = ChannelServiceImpl()

)