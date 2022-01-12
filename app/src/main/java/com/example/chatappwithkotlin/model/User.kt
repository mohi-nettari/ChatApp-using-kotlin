package com.example.chatappwithkotlin.model

class User {
    var name : String? = null
    var email : String? = null
    var pass : String? = null
    var userid : String? = null
    var image : String? = null
    var status : String? = null


    constructor () {
    }

    constructor(name: String?, email: String?, pass: String?, userid: String?, image: String?) {
        this.name = name
        this.email = email
        this.pass = pass
        this.userid = userid
        this.image = image
    }

    constructor(name: String?, email: String?, pass: String?, userid: String?) {
        this.name = name
        this.email = email
        this.pass = pass
        this.userid = userid
    }

    constructor(name: String?, email: String?, pass: String?, userid: String?, image: String?,status: String?) {
        this.name = name
        this.email = email
        this.pass = pass
        this.userid = userid
        this.image = image
        this.status = status


    }


}