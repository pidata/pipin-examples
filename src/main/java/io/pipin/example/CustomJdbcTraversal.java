package io.pipin.example;

import akka.actor.ActorSystem;
import io.pipin.core.poll.JDBCTraversal;
import io.pipin.core.settings.PollSettings;
import org.slf4j.Logger;

/**
 * Created by libin on 2020/4/21.
 */
public class CustomJdbcTraversal extends JDBCTraversal {
    public CustomJdbcTraversal(String uri, String pageParameter, int pageStartFrom, PollSettings pollSettings, ActorSystem actorSystem, Logger log) {
        super(uri, pageParameter, pageStartFrom, pollSettings, actorSystem, log);
    }

    @Override
    public String[] fields() {
        return new String[]{"ISBN", "图书名称", "定价", "库存数","条码","作者","开本","出版社","印次","版次","出版年月","类别名称"};
    }

    @Override
    public String table() {
        return "v_stock_008005003";
    }

    @Override
    public String jdbcPassword() {
        return "lpp@008005003";
    }

    @Override
    public String jdbcUserName() {
        return "user_lpp";
    }

    @Override
    public String jdbcUrl() {
        return "jdbc:oracle:thin:@md.dazo.com.cn:10003:ora224";
    }

    @Override
    public String jdbcDriver() {
        return "oracle.jdbc.driver.OracleDriver";
    }
}
