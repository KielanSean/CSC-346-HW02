
import java.sql.*;

public class Main {


    public static void main(String[] args) {

        String host = "jdbc:mysql://turing.cs.missouriwestern.edu:3306/misc";
        String user = "csc254";
        String password = "age126";

        Connection conn;
        Statement st;
        ResultSet rs;

        try {
            conn = DriverManager.getConnection(host, user, password);
            String queryString = "SELECT * FROM zips LIMIT 10";
            st = conn.createStatement();
            rs =  st.executeQuery(queryString);
            while (rs.next()) {
                System.out.print(rs.getShort("zip_code")+" ");
                System.out.print(" " + rs.getString("city")+"\n");
            }

            conn.close();
        } catch (SQLException e) {
//            e.printStackTrace();
            System.err.println("Failed to connect to " + host);
            System.err.println(e.getMessage());

            System.exit(1);

        }

        System.out.println(calcDistance(42.8944, 39.7675, -93.2119, -94.8467));

    }


    public static double calcDistance(double lat1, double lat2, double lon1, double lon2) {

        double R = 6371e3;
        double l1 = Math.toRadians(lat1);
        double l2 = Math.toRadians(lat2);
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(l1) * Math.cos(l2) *
                Math.sin(dLon/2)*Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        d = d / 1000;
        return Math.round(d*10.0)/10.0;
    }
}



