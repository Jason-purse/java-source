package Log

import java.util.logging.Level
import java.util.logging.LogRecord
import java.util.logging.Logger
import kotlin.math.log

fun main() {
    // Logger
    var global = Logger.getGlobal()
    global.log(Level.INFO,"this is log records!");

    var logger = Logger.getLogger("com.logging.parent")
    logger.level = Level.ALL
    logger.log(LogRecord(Level.INFO,"父亲日志"))
    var logger1 = Logger.getLogger("com.logging.parent.sub")
    logger1.level = Level.ALL

    logger1.parent = logger

    logger.finer("123123");
    logger.config("父亲 config!!!")
    logger1.config("123145345");
    logger.log(Level.CONFIG,"..SDFSDF")
    println("end!!!")

    // 日志打印在控制台的配置需要在java.config.logging.properties中配置级别..
    // 默认有7个级别
    // SEVERE / WARNING / INFO /CONFIG /FINE / FINER / FINEST

}