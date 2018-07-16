package psycho.euphoria.notes


data class Note private constructor(var id: Int,
                                    var content: String,
                                    var createTime: Long,
                                    var lastOprTime: Long) {

}