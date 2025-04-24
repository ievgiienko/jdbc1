package Jdbc1;

public class BeforeWeStart {
    public static void main(String[] args) {
        API api = new MySQL();
        api.connect();
        api.executeQuery("SELECT * from x");
        api.disconnect();

        api = new Oracle();
        api.connect();
        api.executeQuery("SELECT * from y");
        api.disconnect();
    }
}

interface API {
    void connect();
    void executeQuery(String sql);
    void disconnect();
}

class MySQL implements API {
    @Override
    public void connect() {
        System.out.println("Connect to MySQL");
    }

    @Override
    public void executeQuery(String sql) {
        System.out.println("Query MySQL: " + sql);
    }

    @Override
    public void disconnect() {
        System.out.println("Disconnect MySQL");
    }
}

class Oracle implements API {
    @Override
    public void connect() {
        System.out.println("Connect to Oracle");
    }

    @Override
    public void executeQuery(String sql) {
        System.out.println("Query Oracle: " + sql);
    }

    @Override
    public void disconnect() {
        System.out.println("Disconnect Oracle");
    }
}
