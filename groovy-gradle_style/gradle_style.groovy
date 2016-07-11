def task(String taskName){
    println 'execute task ' + taskName
}

def clean(Map type,Closure cl){
    type.type
}

def delete(String path){

}

Delete = 'delete'

task clean(type:Delete){
    delete "path"
}