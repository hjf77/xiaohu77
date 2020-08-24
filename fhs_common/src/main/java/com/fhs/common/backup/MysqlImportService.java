package com.fhs.common.backup;


import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 还原
 */
public class MysqlImportService {

    private String database;
    private String username;
    private String password;
    private String sqlFilePath;
    private String jdbcConnString;
    private String jdbcDriver;
    private Logger logger = LoggerFactory.getLogger(MysqlImportService.class);

    /**
     * @return bool
     * @throws SQLException           exception
     * @throws ClassNotFoundException exception
     */
    public boolean importDatabase() throws SQLException, ClassNotFoundException, IOException {

        if (!this.assertValidParams()) {
            logger.error("Required Parameters not set or empty \n" +
                    "Ensure database, username, password, sqlFilePath params are configured \n" +
                    "using their respective setters");
            return false;
        }


        //connect to the database
        Connection connection;
        if (jdbcConnString == null || jdbcConnString.isEmpty()) {
            connection = MysqlBaseService.connect(username, password,
                    database, jdbcDriver);
        } else {

            if (jdbcConnString.contains("?")) {
                database = jdbcConnString.substring(jdbcConnString.lastIndexOf("/") + 1, jdbcConnString.indexOf("?"));
            } else {
                database = jdbcConnString.substring(jdbcConnString.lastIndexOf("/") + 1);
            }

            logger.debug("database name extracted from connection string: " + database);
            connection = MysqlBaseService.connectWithURL(username, password,
                    jdbcConnString, jdbcDriver);
        }


        // 建立连接
        // 创建ScriptRunner，用于执行SQL脚本
        ScriptRunner runner = new ScriptRunner(connection);
        runner.setErrorLogWriter(null);
        runner.setLogWriter(null);
        FileInputStream fileInputStream = new FileInputStream(sqlFilePath);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
        // 执行SQL脚本
        runner.runScript(inputStreamReader);
        // 关闭连接
        fileInputStream.close();
        inputStreamReader.close();
        connection.close();
        //还原完删除sql 文件
        File file = new File(sqlFilePath);
        if (file.exists()) {
            file.delete();
        }
        // 若成功，打印提示信息
        logger.info("数据还原成功...");
        return true;
    }

    /**
     * This function will check that required parameters
     * are set
     *
     * @return bool
     */
    private boolean assertValidParams() {
        return username != null && !this.username.isEmpty() &&
                password != null && !this.password.isEmpty() &&
                sqlFilePath != null && !this.sqlFilePath.isEmpty() &&
                ((database != null && !this.database.isEmpty()) || (jdbcConnString != null && !jdbcConnString.isEmpty()));
    }

    /**
     * This function will create a new
     * MysqlImportService instance thereby facilitating
     * a builder pattern
     *
     * @return MysqlImportService
     */
    public static MysqlImportService builder() {
        return new MysqlImportService();
    }

    public MysqlImportService setDatabase(String database) {
        this.database = database;
        return this;
    }

    public MysqlImportService setUsername(String username) {
        this.username = username;
        return this;
    }

    public MysqlImportService setPassword(String password) {
        this.password = password;
        return this;
    }

    public MysqlImportService setsqlFilePath(String sqlFilePath) {
        this.sqlFilePath = sqlFilePath;
        return this;
    }


    public MysqlImportService setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
        return this;
    }

    public MysqlImportService setJdbcConnString(String jdbcConnString) {
        this.jdbcConnString = jdbcConnString;
        return this;
    }
}
