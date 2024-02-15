package kg.edu.yjut.litenote.bean

data class ChannelInfo(
    var channelName: String,
    var channelStr: String,
)
// mipush

data class LogsInfo(
    var insertTime: String,
    var logs: ArrayList<Log>,
    var packageName: String,
)

data class Log(
    var id: Int,
    var title : String,
    var content: String,

    var channelName: String,

)