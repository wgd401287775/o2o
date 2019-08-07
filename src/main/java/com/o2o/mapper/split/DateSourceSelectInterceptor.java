package com.o2o.mapper.split;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Locale;
import java.util.Properties;

public class DateSourceSelectInterceptor implements Interceptor {

    /**正则匹配 insert、delete、update操作*/
    private static final String REGEX = ".*insert\\\\u0020.*|.*delete\\\\u0020.*|.*update\\\\u0020.*";
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //判断当前操作是否有事务
        boolean synchonizationActive = TransactionSynchronizationManager.isActualTransactionActive();
        //获取执行参数
        Object[] objects = invocation.getArgs();
        MappedStatement ms = (MappedStatement) objects[0];
        //默认设置使用主库
        String lookupKey = DynamicDataSourceHolder.DB_MASTER;
        if(!synchonizationActive){
            //读方法
            if(ms.getSqlCommandType().equals(SqlCommandType.SELECT)){
                //selectKey为自增主键（SELECT LAST_INSERT_ID()）方法,使用主库
                if(ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)){
                    lookupKey = DynamicDataSourceHolder.DB_MASTER;
                } else {
                    BoundSql boundSql = ms.getSqlSource().getBoundSql(objects[1]);
                    String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", " ");
                    //如果是insert、delete、update操作 使用主库
                    if(sql.matches(REGEX)){
                        lookupKey = DynamicDataSourceHolder.DB_MASTER;
                    } else {
                        //使用从库
                        lookupKey = DynamicDataSourceHolder.DB_SLAVE;
                    }
                }
            }
        } else {
            //一般使用事务的都是写操作，直接使用主库
            lookupKey = DynamicDataSourceHolder.DB_MASTER;
        }
        //设置数据源
        DynamicDataSourceHolder.setDataSourceType(lookupKey);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if(target instanceof Executor) {
            //如果是Executor（执行增删改查操作），则拦截下来
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
