package com.qin.springboot.a002;

import com.qin.springboot.dao.UserDao;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataSourceAspect {

    /**
     * 使用空方法定义切点表达式
     */
    @Pointcut("execution(* com.qin.springboot.dao.*.*(..))")
    public void declareJointPointExpression() {
    }

    /**
     * 使用定义切点表达式的方法进行切点表达式的引入
     */
    @Before("declareJointPointExpression()")
    public void setDataSourceKey(JoinPoint point) {
        // 连接点所属的类实例是ShopDao
        if (point.getTarget() instanceof UserDao) {
            RoutingDataSource.setThreadLocalDataSourceKey(DataSourceType.writeDataSource.name());
        } else {// 连接点所属的类实例是UserDao（当然，这一步也可以不写，因为defaultTargertDataSource就是该类所用的mytestdb）
            //DatabaseContextHolder.setDatabaseType(DatabaseType.mytestdb);
        }
    }

//    @Around("execution(* com.xxx.firstboot.dao.*.*(..))")
//    public Object setDataSourceKeyByAround(ProceedingJoinPoint point) throws Throwable{
//        if(point.getTarget() instanceof ShopDao){
//            DatabaseContextHolder.setDatabaseType(DatabaseType.mytestdb2);
//        }else{//连接点所属的类实例是UserDao（当然，这一步也可以不写，因为defaultTargertDataSource就是该类所用的mytestdb）
//            DatabaseContextHolder.setDatabaseType(DatabaseType.mytestdb);
//        }
//        return point.proceed();//执行目标方法
//    }

}