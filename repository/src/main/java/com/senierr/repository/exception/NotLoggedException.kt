package com.senierr.repository.exception

import java.io.IOException

/**
 * 未登录异常
 *
 * @author zhouchunjie
 * @date 2020/6/22
 */
class NotLoggedException : IOException("You are not logged in yet, please log in first.")