package com.example.chatappwithkotlin.model

class Imagemsg {



    var imageuri : String? = null
    var uid : String? = null
    var receiverid : String? = null
    var timstamp : Long? = null
    var isSeen : Boolean? = null
    var type : String? = null


    constructor() {
    }

    constructor(
        imageuri : String?,
        uid: String?,
        receiverid: String?,
        timstamp: Long?,
        isSeen: Boolean?,
        type: String?
    ) {
        this.uid = uid
        this.receiverid = receiverid
        this.timstamp = timstamp
        this.isSeen = isSeen
        this.type = type
    }


    constructor( imageuri : String?,senderid: String?, receiverid: String?,type: String?) {
        this.uid = uid
        this.receiverid = receiverid
        this.type = type
    }

    constructor(
        imageuri : String?,
        uid: String?,
        receiverid: String?,
        isSeen: Boolean?,
        type: String?
    ) {
        this.uid = uid
        this.receiverid = receiverid
        this.isSeen = isSeen
        this.type = type

    }

}