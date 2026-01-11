package com.adv;

import com.zaxxer.hikari.*;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


/**
 * Die Database-Klasse ist fuer die Verwaltung der Datenbankverbindungen zustaendig.
 * Implementiert Connection Pooling mit HikariCP, um eine effizientere Verbindung zur Datenbank zu haben.
 * Es werden 10 Verbindungen im Pool bereitgehalten.
 * Die Configs der Datenbank erfolgt ueber eine config.PROPERTIES Datei.
 * **/
public class Database {
    
    private static final Properties PROPERTIES = new Properties();
    private static HikariDataSource dataSource;

    // static - Block: Wird direkt beim Start ausgeführt.
    static {
        try (InputStream input = Database.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("Error: unable to find config.PROPERTIES. Make sure it's in the resources folder.");
            }
            PROPERTIES.load(input);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(PROPERTIES.getProperty("db.url"));
            config.setUsername(PROPERTIES.getProperty("db.user"));
            config.setPassword(PROPERTIES.getProperty("db.password"));

            config.setMaximumPoolSize(10); // Max. 10 gleichzeitige Verbindungen
            config.setMinimumIdle(2); // Immer min. 2 Verbindungen bereithalten
            config.setIdleTimeout(30000); // Nach 30sek Inaktivität Verbindung trennen
            config.setConnectionTimeout(3000); // Max. 3 Sekunden warten auf freie Verbindung

            dataSource = new HikariDataSource(config);
            System.out.println("Database connection Pool started.");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**@return Gibt eine Verbindung aus dem Connection Pool zurueck.**/
    public Connection connect() throws SQLException {
        // Wenn conn.close() aufgerufen wird -> Verbindung wieder zurück in den Pool
        return dataSource.getConnection();
    }

}
