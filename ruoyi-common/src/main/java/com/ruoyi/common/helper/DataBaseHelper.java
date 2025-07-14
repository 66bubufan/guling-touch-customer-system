package com.ruoyi.common.helper;

import cn.hutool.core.convert.Convert;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.ruoyi.common.enums.DataBaseType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.spring.SpringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * 数据库助手
 *
 * @author Lion Li
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataBaseHelper {

    private static DynamicRoutingDataSource DS;

    /**
     * 获取动态路由数据源，如果不可用则返回null
     */
    private static DynamicRoutingDataSource getDynamicRoutingDataSource() {
        if (DS == null) {
            try {
                DS = SpringUtils.getBean(DynamicRoutingDataSource.class);
            } catch (Exception e) {
                // Spring容器未初始化时，DS为null
                return null;
            }
        }
        return DS;
    }

    /**
     * 获取当前数据库类型
     */
    public static DataBaseType getDataBaseType() {
        try {
            DynamicRoutingDataSource dataSource = getDynamicRoutingDataSource();
            if (dataSource == null) {
                return DataBaseType.MY_SQL; // 默认返回MySQL类型
            }
            DataSource ds = dataSource.determineDataSource();
            try (Connection conn = ds.getConnection()) {
                DatabaseMetaData metaData = conn.getMetaData();
                String databaseProductName = metaData.getDatabaseProductName();
                return DataBaseType.find(databaseProductName);
            }
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public static boolean isMySql() {
        return DataBaseType.MY_SQL == getDataBaseType();
    }

    public static boolean isOracle() {
        return DataBaseType.ORACLE == getDataBaseType();
    }

    public static boolean isPostgerSql() {
        return DataBaseType.POSTGRE_SQL == getDataBaseType();
    }

    public static boolean isSqlServer() {
        return DataBaseType.SQL_SERVER == getDataBaseType();
    }

    public static String findInSet(Object var1, String var2) {
        DataBaseType dataBasyType = getDataBaseType();
        String var = Convert.toStr(var1);
        if (dataBasyType == DataBaseType.SQL_SERVER) {
            // charindex(',100,' , ',0,100,101,') <> 0
            return "charindex('," + var + ",' , ','+" + var2 + "+',') <> 0";
        } else if (dataBasyType == DataBaseType.POSTGRE_SQL) {
            // (select position(',100,' in ',0,100,101,')) <> 0
            return "(select position('," + var + ",' in ','||" + var2 + "||',')) <> 0";
        } else if (dataBasyType == DataBaseType.ORACLE) {
            // instr(',0,100,101,' , ',100,') <> 0
            return "instr(','||" + var2 + "||',' , '," + var + ",') <> 0";
        }
        // find_in_set('100' , '0,100,101')
        return "find_in_set('" + var + "' , " + var2 + ") <> 0";
    }
}
