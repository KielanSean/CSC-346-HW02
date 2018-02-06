
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static double maxLat;
    public static double maxLon;
    public static double minLon;
    public static double minLat;

    public static void main(String[] args) {

        String host = "jdbc:mysql://turing.cs.missouriwestern.edu:3306/misc";
        String user = "csc254";
        String password = "age126";
        City centerCity = new City("Hello", "64505", 42.89,-93.21);
        ArrayList<City> cities = new ArrayList<City>();
        Connection conn;
        Statement st;
        ResultSet rs;
        String zipC;
        String dis = "30000";

//        centerCity = new City("Saint Joseph", "64505", 49.32, 89.215);
//        getDistance(Integer.parseInt(dis), centerCity.getLat(), centerCity.getLon());
//        String distanceQuery = String.format("SELECT * FROM zips WHERE\n\t(lat >= %.2f AND lat<= %.2f) AND (lon >= %.2f " +
//                        "AND lon <= %.2f)\nAND\n\tacos(sin(%.2f) * sin(%.2f) + cos(%.2f) * cos(%.2f) *cos(%.2f - (%.2f))) " +
//                        "<= %.2f;",Math.toDegrees(minLat), Math.toDegrees(maxLat), Math.toDegrees(minLon), Math.toDegrees(maxLon)
//                , Math.toDegrees(1.3963), centerCity.getLon(), Math.toDegrees(1.3963),centerCity.getLat(),
//                centerCity.getLon(),Math.toDegrees(-0.6981), Math.toDegrees(0.570) ) ;
//
//        System.out.println(distanceQuery);
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the zip code");
        zipC = scan.nextLine();
        System.out.println("Enter distance");
        dis = scan.nextLine();

        try {
            conn = DriverManager.getConnection(host, user, password);
            String queryString = "SELECT * FROM zips WHERE zip_code LIKE "+zipC+" LIMIT 10";
            st = conn.createStatement();
            rs =  st.executeQuery(queryString);
            while (rs.next()) {
                boolean isPrime;
                String name = rs.getString("city");
                String zipcode = rs.getString("zip_code");
                double lat1 = rs.getDouble("lat");
                double lon1 = rs.getDouble("lon");
                isPrime = rs.getBoolean("z_primary");
                if (isPrime) {
                    centerCity = new City(name, zipcode, lat1, lon1);
                    cities.add(centerCity);
                    break;
                }
            }

            getDistance(Integer.parseInt(dis), centerCity.getLat(), centerCity.getLon());

            String distanceQuery = String.format("SELECT * FROM zips WHERE\n\t(lat >= %.2f AND lat<= %.2f) AND (lon >= %.2f " +
                    "AND lon <= %.2f)\nAND\n\tacos(sin(%.2f) * sin(%.2f) + cos(%.2f) * cos(%.2f) *cos(%.2f - (%.2f))) " +
                    "<= %.2f;",Math.toDegrees(minLat), Math.toDegrees(maxLat), Math.toDegrees(minLon), Math.toDegrees(maxLon)
                    , Math.toDegrees(1.3963), centerCity.getLon(), Math.toDegrees(1.3963),centerCity.getLat(),
                    centerCity.getLon(),Math.toDegrees(-0.6981), Math.toDegrees(0.570) ) ;

            st = conn.createStatement();
            rs = st.executeQuery(distanceQuery);
            while (rs.next()) {
                boolean isPrime;
                String name = rs.getString("city");
                String zipcode = rs.getString("zip_code");
                double lat1 = rs.getDouble("lat");
                double lon1 = rs.getDouble("lon");
                isPrime = rs.getBoolean("z_primary");
                int taNum = 0;
                if (isPrime) {
                    City toAdd = new City(name, zipcode, lat1, lon1);
                    for (City currentCity : cities) {
                        if (currentCity.getName().equals(toAdd.getName())) {
                            taNum =1;
                            break;
                        }
                    }
                    if (taNum == 1) {
                        break;
                    } else {
                        cities.add(toAdd);
                    }

//                    if (cities.contains(toAdd.getName())) {
//                        break;
//                    }else{
//                        cities.add(toAdd);
//                    }
                }

            }


            for (int i = 0; i < cities.size(); i++) {
                cities.get(i).print();

            }
            conn.close();
        } catch (SQLException e) {
            System.err.println("Failed to connect to " + host);
            System.err.println(e.getMessage());

            System.exit(1);

        }

        System.out.println(calcDistance(42.8944, 39.7675, -93.2119, -94.8467));

    }

    public static void getDistance(int distance, double givenLat, double givenLon) {
        double r = Math.toRadians(distance / 3958.76);
        minLat = Math.toRadians(givenLat) - r;
        maxLat = Math.toRadians(givenLat) + r;
//        double Tlat = Math.asin((Math.sin(Math.toRadians(givenLat)) / Math.cos(r)));
//        double Dlon = (Math.acos((Math.cos(r)) - (Math.sin(Tlat)) * Math.sin(Math.toRadians(givenLat)))) /
//                ((Math.cos(Tlat)) * Math.cos(Math.toRadians(givenLat)));
        double Dlon = Math.asin((Math.sin(r)) / Math.cos(Math.toRadians(givenLat)));
        minLon = Math.toRadians(givenLon) - Dlon;
        maxLon = Math.toRadians(givenLon) + Dlon;
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


class City {
    protected String name, zip;
    protected double lat, lon;

    public City(String name, String zip, double lat, double lon) {
        this.name = name;
        this.zip = zip;
        this.lat = lat;
        this.lon = lon;
    }

    public City() {}

    public String getName(){
        return name;}
    public double getLat() {
        return lat;
    }
    public double getLon() {
        return lon;
    }
    public void print() {
        System.out.printf("%s with Zip Code: %s at %.2f, %.2f\n", name, zip, lat, lon);
    }
}
