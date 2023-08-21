plugins {
    id("com.bmuschko.docker-remote-api")
}
class DemoPlugin : Plugin<Project>{

    override fun apply(target: Project) {
        target.tasks.register("greeting"){
            doLast {
                println("hello world~")
            }
        }
    }
}
apply<DemoPlugin>()