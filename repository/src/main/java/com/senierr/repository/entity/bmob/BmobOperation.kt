package com.senierr.repository.entity.bmob

/**
 * Bmob操作
 *
 * @author zhouchunjie
 * @date 2020/5/7
 */
sealed class BmobOperation(val __op: String) {

    // 原子计算器操作
    class Increment(var amount: Int): BmobOperation("Increment")

    // 删除操作
    class Delete: BmobOperation("Delete")
}