package com.senierr.repository.entity.bmob

/**
 * 一对一/多关联
 *
 * @author zhouchunjie
 * @date 2020/5/7
 */
sealed class BmobRelation(val __op: String) {

    data class Add(val objects: MutableList<BmobPoint>): BmobRelation("AddRelation")

    data class Delete(val objects: MutableList<BmobPoint>): BmobRelation("RemoveRelation")
}