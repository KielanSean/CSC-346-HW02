public class Main {


    public static void main(String[] args) {
	// write your code here
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
        return d / 1000;
    }
}



