package module06.xtracted.controller

interface  Controller<A: Action> {
    fun triggerAction(action: A)
}