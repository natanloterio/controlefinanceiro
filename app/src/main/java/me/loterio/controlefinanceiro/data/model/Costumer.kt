package me.loterio.controlefinanceiro.data.model

data class Costumer (
    var id: String = "",
    var name: String = "",
    var address: String = "",
    var phone: String = ""
){
    constructor(): this("","","","")
}

