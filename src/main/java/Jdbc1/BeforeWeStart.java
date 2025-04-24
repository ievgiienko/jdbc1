package Jdbc1;

public class BeforeWeStart {
    public static void main(String[] args) {
        System.out.println("Example 1");

        try {
            System.out.println("Hello");
            System.out.println("world");
        } finally {
            System.out.println("finish");
        }

        System.out.println();
        System.out.println("Example 2");

        try {
            System.out.println(10 / 0);
            System.out.println("success"); // !!
        } catch (Exception ex) {
            System.out.println(ex);
        }

        System.out.println();
        System.out.println("Example 3");

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
