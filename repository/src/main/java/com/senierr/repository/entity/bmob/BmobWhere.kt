package com.senierr.repository.entity.bmob

/**
 * Bmob条件
 *
 * @author zhouchunjie
 * @date 2020/5/7
 */
sealed class BmobWhere {

    companion object {
        const val LT = "\$lt"           // 小于
        const val LTE = "\$lte"         // 小于等于
        const val GT = "\$gt"           // 大于
        const val GTE = "\$gte"         // 大于等于
        const val NE = "\$ne"           // 不等于
        const val IN = "\$in"           // 包含在数组中
        const val NIN = "\$nin"         // 不包含在数组中
        const val EXISTS = "\$exists"   // 这个Key有值
        const val ALL = "\$all"         // 包括所有给定的值
        const val REGEX = "\$regex"     // 匹配PCRE表达式
    }

    // 等于
    class Equal(property: String, value: String): BmobWhere()

    // 比较
    class Compare(property: String, where: MutableMap<String, *>): BmobWhere()

    // 或
    class Or(val name: String = "\$or", vararg compares: Compare): BmobWhere()

    // 且
    class And(val name: String = "\$and", vararg compares: Compare): BmobWhere()
}