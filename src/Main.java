
import java.sql.*;
import java.util.*;

public class Main {

    public static double maxLat;
    public static double maxLon;
    public static double minLon;
    public static double minLat;
    public static int pop;
    public static int housing;

    public static void main(String[] args) {

        String host = "jdbc:mysql://turing.cs.missouriwestern.edu:3306/misc";
        String user = "csc254";
        String password = "age126";
        City centerCity = new City();
        Set<City> cities = new HashSet<>();
        Connection conn;
        Statement st;
        ResultSet rs;
        String zipC;
        String dis;
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
                String state = rs.getString("state_prefix");
                double lat1 = rs.getDouble("lat");
                double lon1 = rs.getDouble("lon");
                isPrime = rs.getBoolean("z_primary");
                int pop1 = rs.getInt("population");
                int house1 = rs.getInt("housingunits");
                if (isPrime) {
                    centerCity = new City(name, state, zipcode, lat1, lon1, pop1, house1);
                    cities.add(centerCity);
                    break;
                }
            }

            getDistance(Integer.parseInt(dis), centerCity.getLat(), centerCity.getLon());

            String distanceQuery = String.format("SELECT * FROM zips WHERE\n\t(lat >= %.2f AND lat<= %.2f) AND " +
                            "(lon >= %.2f AND lon <= %.2f)\nAND\n\tacos(sin(%.2f) * sin(lat) + cos(%.2f) * cos(lat) * " +
                            "cos(lon - (%.2f))) <= %.2f;",Math.toDegrees(minLat), Math.toDegrees(maxLat),
                            Math.toDegrees(minLon), Math.toDegrees(maxLon), Math.toDegrees(1.3963),
                            Math.toDegrees(1.3963),Math.toDegrees(-0.6981), Math.toDegrees(0.570) ) ;

            st = conn.createStatement();
            rs = st.executeQuery(distanceQuery);
            while (rs.next()) {
                boolean isPrime;
                String name = rs.getString("city");
                String zipcode = rs.getString("zip_code");
                String state = rs.getString("state_prefix");
                double lat1 = rs.getDouble("lat");
                double lon1 = rs.getDouble("lon");
                isPrime = rs.getBoolean("z_primary");
                int pop1 = rs.getInt("population");
                int house1 = rs.getInt("housingunits");
                int taNum = 0;
                if (isPrime) {
                    City toAdd = new City(name, state, zipcode, lat1, lon1, pop1, house1);
                    cities.add(toAdd);
                }
            }
            Set<City> set = new TreeSet<City>(new Comparator<City>() {
                @Override
                public int compare(City o1, City o2) {
                    if (o1.getName().equals(o2.getName()) && o1.getState().equals(o2.getState())) {

                        return 0;
                    }
                    return 1;
                }
            });
            set.addAll(cities);

            for (City city: set) {
                pop += city.getPopulation();
                housing += city.getHousingUnits();
                double disFrom = calcDistance(centerCity.getLat(), city.getLat(), centerCity.getLon(), city.getLon());
                System.out.printf("%s, %s with Zip Code: %s at %.2f, %.2f %.2f From %s, %s\n", city.getName(),
                        city.getState(), city.getZip(), city.getLat(), city.getLon(), disFrom, centerCity.getName()
                        , centerCity.getState());
            }

            System.out.printf("\nPopulation of cities affected: %d\nHousing Units in cities affected: %d\n", pop, housing);

            conn.close();
        } catch (SQLException e) {
            System.err.println("Failed to connect to " + host);
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void getDistance(int distance, double givenLat, double givenLon) {
        double r = (distance / 3958.76);
        minLat = Math.toRadians(givenLat) - r;
        maxLat = Math.toRadians(givenLat) + r;
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
        d = d / 1.609344;
        return Math.round(d*10.0)/10.0;
    }
}


class City {
    protected String name, zip, state;
    protected double lat, lon;
    protected int population, housingUnits;


    public City(String name, String state, String zip, double lat, double lon, int population, int housingUnits) {
        this.name = name;
        this.zip = zip;
        this.state = state;
        this.lat = lat;
        this.lon = lon;
        this.population = population;
        this.housingUnits = housingUnits;
    }

    public City() {}

    public String getName(){
        return name;}
    public String getState(){
        return state;}

    public String getZip() {
        return zip;
    }
    public double getLat() {
        return lat;
    }
    public double getLon() {
        return lon;
    }

    public int getPopulation(){
        return population;}

    public int getHousingUnits() {
        return housingUnits;
    }
    public void print() {
        System.out.printf("%s, %s with Zip Code: %s at %.2f, %.2f\n", name, state, zip, lat, lon);
    }
}
