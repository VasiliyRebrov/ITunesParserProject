package com.data.common

import java.lang.Exception

class NotFoundException(msg: String = EXC_NOT_FOUND_MSG) : Exception(msg)
class NoNetworkException(msg: String = EXC_NO_NETWORK_MSG) : Exception(msg)