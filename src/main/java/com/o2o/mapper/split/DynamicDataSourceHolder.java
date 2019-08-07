package com.o2o.mapper.split;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicDataSourceHolder {

    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceHolder.class);

    /**用来存取key，ThreadLocal保证了线程安全*/
    private static ThreadLocal<String> contextHolder = new ThreadLocal<>();
    /**主库*/
    public static final String DB_MASTER = "master";
    /**从库*/
    public static final String DB_SLAVE = "slave";

    /**
     * 获取线程的数据源
     * @return
     */
    public static String getDataSourceType(){
        String db = contextHolder.get();
        if(db == null) {
            //如果db为空则默认使用主库（因为主库支持读和写）
            db = DB_MASTER;
        }
        return db;
    }

    /**
     * 设置线程的数据源
     * @param db
     */
    public static void setDataSourceType(String db){
        contextHolder.set(db);
        logger.info("所使用的数据源为：" + db);
    }

    /**
     * 清理连接状态
     */
    public static void cleanDataSource(){
        contextHolder.remove();
    }
}
