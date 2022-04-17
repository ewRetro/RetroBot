package com.retrogray.retrobot.database;

public interface DatabaseManager {
    DatabaseManager INSTANCE = new SQLiteDataSource();

    // prefix settings
    String getPrefix(long guildId);
    void setPrefix(long guildId, String newPrefix);
}
