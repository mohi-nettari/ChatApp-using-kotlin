package com.example.chatappwithkotlin.model

import java.util.*

class Messages {

    var msg : String? = null
    var uid : String? = null
    var receiverid : String? = null
    var messageid : String? = null
    var timstamp : Date? = null
    var isSeen : Boolean? = null
    var type : String? = null


    constructor() {
    }

    constructor(
        msg: String?,
        uid: String?,
        receiverid: String?,
        messageid : String?,
        timstamp: Date?,
        isSeen: Boolean?,
        type: String?
        ) {
        this.msg = msg
        this.uid = uid
        this.receiverid = receiverid
        this.messageid = messageid
        this.timstamp = timstamp
        this.isSeen = isSeen
        this.type = type
    }


    constructor( msg: String,senderid: String?, receiverid: String?,type: String?) {
        this.msg = msg
        this.uid = uid
        this.receiverid = receiverid
        this.type = type
    }


    constructor(
        msg: String?,
        uid: String?,
        receiverid: String?,
        isSeen: Boolean?,
        type: String?
    ) {
        this.msg = msg
        this.uid = uid
        this.receiverid = receiverid
        this.isSeen = isSeen
        this.type = type

    }
    constructor(
        msg: String?,
        uid: String?,
        receiverid: String?,
        isSeen: Boolean?,
        type: String?,
        messageid: String?
    ) {
        this.msg = msg
        this.uid = uid
        this.receiverid = receiverid
        this.isSeen = isSeen
        this.type = type
        this.messageid = messageid

    }



    constructor(
        msg: String?,
        uid: String?,
        receiverid: String?,
        timstamp: Date?,
        isSeen: Boolean?,
        type: String?
    ) {
        this.msg = msg
        this.uid = uid
        this.receiverid = receiverid
        this.timstamp = timstamp
        this.isSeen = isSeen
        this.type = type
    }

    constructor(
        msg: String?,
        uid: String?,
        receiverid: String?,
        timstamp: Date?,
        isSeen: Boolean?,
        type: String?,
        messageid: String?
    ) {
        this.msg = msg
        this.uid = uid
        this.receiverid = receiverid
        this.timstamp = timstamp
        this.isSeen = isSeen
        this.type = type
        this.messageid = messageid
    }



}