package kg.edu.yjut.litenote.bean

//         String sql2 = "create table logs(" +
//                "id integer primary key autoincrement," +
//                "title varchar(64)," +
//                "content varchar(256)," +
//                "package_name varchar(256)," +
//                "channel_name varchar(256)," +
//                "insert_time datetime default (datetime('now', 'localtime'))" +
//                ")";
data class LogBeam(
    var id: Int = 0,
    var title: String,
    var content: String= "",
    var packageName: String = "",
    var channelName: String = "",
    var insertTime: String = "",
)