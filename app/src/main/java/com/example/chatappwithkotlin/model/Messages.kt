package com.example.chatappwithkotlin.model

class Messages {

    var msg : String? = null
    var uid : String? = null
    var receiverid : String? = null
    var messageid : String? = null
    var timstamp : Long? = null
    var isSeen : Boolean? = null


    constructor() {
    }

    constructor(
        msg: String?,
        uid: String?,
        receiverid: String?,
        messageid: String?,
        timstamp: Long?,
        isSeen: Boolean?
    ) {
        this.msg = msg
        this.uid = uid
        this.receiverid = receiverid
        this.messageid = messageid
        this.timstamp = timstamp
        this.isSeen = isSeen
    }


    constructor( msg: String,senderid: String?, receiverid: String?) {
        this.msg = msg
        this.uid = uid
        this.receiverid = receiverid
    }

    constructor(
        msg: String?,
        uid: String?,
        receiverid: String?,
        isSeen: Boolean?
    ) {
        this.msg = msg
        this.uid = uid
        this.receiverid = receiverid
        this.isSeen = isSeen

    }



}