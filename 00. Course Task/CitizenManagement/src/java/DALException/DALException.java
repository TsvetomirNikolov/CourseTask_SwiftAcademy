package DALException;

import java.sql.SQLException;

public class DALException extends SQLException {

    public DALException(String reason) {
        super(reason);
    }

    public DALException(String reason, String sqlState, Throwable cause) {
        super(reason, sqlState, cause);
    }
}
