package de.g2p.ToSe_Parkapp.Service;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Getter
@Component
public class DBService {
    Connection connection;

    public DBService() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "root");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Parkapp?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", connectionProps);
        System.out.println("Connected to database");

    }
}
