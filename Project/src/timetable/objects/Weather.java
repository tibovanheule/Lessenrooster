package timetable.objects;

/**
 * Object Weather, holds all weather data
 *
 * @author Tibo Vanheule
 */
public class Weather {
    private Double temp;
    private Double windDegree;
    private Double windSpeed;
    private String humidity;
    private String city;
    private String condition;
    private Boolean connection;
    private String icon;

    public Weather(Boolean connection, Double temp, Double windDegree, Double windSpeed, String humidity, String icon, String condition, String city) {
        this.temp = temp;
        this.humidity = humidity;
        this.windDegree = windDegree;
        this.windSpeed = windSpeed;
        this.connection = connection;
        this.icon = icon;
        this.condition = condition;
        this.city = city;
    }

    public Weather(Boolean connection) {
        this.connection = connection;
    }

    public String getCity() {
        return city;
    }

    public String getCondition() {
        return condition;
    }

    public Boolean getConnection() {
        return connection;
    }

    public String getIcon() {
        return icon;
    }

    public Double getTemp() {
        return temp;
    }

    public Double getWindDegree() {
        return windDegree;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public String getHumidity() {
        return humidity;
    }
}
