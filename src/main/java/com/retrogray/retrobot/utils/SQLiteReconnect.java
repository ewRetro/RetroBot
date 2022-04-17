package com.retrogray.retrobot.utils;

import com.retrogray.retrobot.database.SQLiteDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class SQLiteReconnect {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLiteReconnect.class);

    public void reconnect() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        LOGGER.info("Database connection re-established!");
    }
}
