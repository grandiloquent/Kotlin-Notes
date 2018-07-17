package psycho.euphoria.notes


data class Note constructor(var id: Long?,
                            var title: String?,
                            var content: String?,
                            var createTime: Long?,
                            var lastOprTime: Long?,
                            var trash: Int?) {

    companion object {
        fun newInstance() = Note(null, null, null, null, null, null)
    }
}