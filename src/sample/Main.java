package sample;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Main extends Application {

    /*
    {"coord":{"lon":-0.13,"lat":51.51},"weather":[{"id":803,"main":"Clouds","description":"broken clouds","icon":"04d"}],"base":"stations","main":{"temp":19.11,"feels_like":12.33,"temp_min":18.33,"temp_max":20,"pressure":1010,"humidity":56},"visibility":10000,"wind":{"speed":9.8,"deg":270},"clouds":{"all":75},"dt":1593939628,"sys":{"type":1,"id":1414,"country":"GB","sunrise":1593921051,"sunset":1593980346},"timezone":3600,"id":2643743,"name":"London","cod":200}
    */
    //KEY
    //8658de70c460efa547dec60285d833fd
    //
    int width = 300;
    int height = 500;
    Group weatherGroup = new Group();
    Scene weatherScene = new Scene(weatherGroup, width, height);
    Group settingGroup = new Group();
    Scene settingScene = new Scene(settingGroup, width, height);
    //https://api.openweathermap.org/data/2.5/weather?q=London,uk&units=metric&appid=8658de70c460efa547dec60285d833fd
    String API_Key = "8658de70c460efa547dec60285d833fd";
    String location, errorLocation;
    String countryCode, errorCode;
    Scanner scanner = new Scanner(System.in);
    ArrayList<WeatherData> weatherData = new ArrayList<>();
    WeatherData currentWeather;
    //
    TextField codeField;
    TextField locationField;
    Text warning;
    //
    Label locationLBL=new Label(),weatherLBL=new Label(),tempLBL=new Label(),HtoL=new Label();
    int LBL_buffer = 5;
    /*
    60 calls/minute
    1,000,000 calls/month
    */
    @Override
    public void start(Stage stage) throws Exception {
        //
        countryCode = "gb";
        //location = scanner.nextLine();
        location = "Newbury";
        //
        //System.out.println("What country do you want to check");
        //countryCode = scanner.nextLine();
        //
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "," + countryCode + "&units=metric&appid=" + API_Key;
        //urlString ="https://api.openweathermap.org/data/2.5/weather?q=London,uk&units=metric&appid=8658de70c460efa547dec60285d833fd";

        getWeatherInfo(urlString);


        settingsSetup(stage);

        weatherSetup(stage);


        System.out.println("---------------------------------------------------------------------------");
        //
        timelineStarter();


        stage.setScene(weatherScene);
        stage.show();
    }

    private void weatherSetup(Stage stage) {
        //

        Button weather = new Button("Weather");
        weather.relocate(0, 0);
        //
        weather.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY:

                    boolean swap;
                    if (!(codeField.getText().equalsIgnoreCase(errorCode) && locationField.getText().equalsIgnoreCase(errorLocation))) {
                        System.out.println("Changed to Weather Scene");

                        errorCode = null;
                        errorLocation = null;

                        countryCode = codeField.getText();
                        location = locationField.getText();
                        location = location.substring(0, 1).toUpperCase() + location.substring(1);
                        update();
                        swap = updateWeather();

                        if (!swap) {//if no error - swap scene
                            stage.setScene(weatherScene);
                            warning.setVisible(false);
                            error = false;
                        } else {//if error show warning
                            warning.setVisible(true);
                        }
                    }
                    break;
            }
        });
        //
        update();

        weatherGroup.getChildren().addAll(locationLBL, weatherLBL,tempLBL);
        settingGroup.getChildren().addAll(weather);
    }

    private void update() {
        locationLBL.setText(location);
        locationLBL.relocate((int) ((width / 2) - new Text(location).getBoundsInParent().getWidth() / 2), (int) ((75) - new Text(location).getBoundsInParent().getHeight() / 2));
        //
        weatherLBL.setText(weatherData.get(weatherData.size() - 1).getWeather());
        weatherLBL.relocate((int) ((width / 2) - new Text(weatherLBL.getText()).getBoundsInParent().getWidth() / 2), (int) ((locationLBL.getLayoutY() + new Text(location).getBoundsInParent().getHeight() + LBL_buffer) - new Text(weatherLBL.getText()).getBoundsInParent().getHeight() / 2));
        //
        tempLBL.setText(String.valueOf(new DecimalFormat("#.#").format(((weatherData.get(weatherData.size() - 1).getTemp())))) + "°");
        tempLBL.relocate((int) ((width / 2) - new Text(tempLBL.getText()).getBoundsInParent().getWidth() / 2), (int) ((weatherLBL.getLayoutY() + new Text(weatherLBL.getText()).getBoundsInParent().getHeight() + LBL_buffer) - new Text(tempLBL.getText()).getBoundsInParent().getHeight() / 2));

    }

    boolean error = false;

    private void settingsSetup(Stage stage) {
        Button settings = new Button("Settings");
        settings.relocate(0, 0);
        //
        settings.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    stage.setScene(settingScene);
                    System.out.println("Changed to Settings Scene");
                    break;
            }
        });
        //
        Text countryText = new Text("Update country code:");
        countryText.relocate(0, 50);

        //
        codeField = new TextField(countryCode);
        codeField.relocate(countryText.getBoundsInLocal().getMaxX() + 10, countryText.getBoundsInParent().getMinY() - 3);
        codeField.setPrefWidth(50);
        //
        Text locationText = new Text("Update location:");
        locationText.relocate(0, 150);
        //
        locationField = new TextField(location);
        locationField.relocate(locationText.getBoundsInLocal().getMaxX() + 10, locationText.getBoundsInParent().getMinY() - 3);
        locationField.setPrefWidth(100);

        warning = new Text("Location not found - please check");
        warning.relocate(0, 90);
        warning.setVisible(false);

        weatherGroup.getChildren().addAll(settings);
        //
        settingGroup.getChildren().addAll(countryText, codeField, locationText, locationField, warning);
    }

    private void timelineStarter() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(5), event -> {

            updateWeather();
            update();

        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();


    }

    private boolean updateWeather() {
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "," + countryCode + "&units=metric&appid=" + API_Key;
        //
        System.out.println("---------------------------------------------------------------------------");
        return getWeatherInfo(urlString);

    }

    private boolean getWeatherInfo(String urlString) {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
                //weatherData.add(new WeatherData(line));
            }
            reader.close();
            //System.out.println(result);

            JsonObject jsonObject = new JsonParser().parse(String.valueOf(result)).getAsJsonObject();

            float lon;
            float lat;
            String weather;
            float temp, feels_like, temp_min, temp_max, pressure, humidity;
            float speed, deg, gust;
            //
            lon = jsonObject.getAsJsonObject("coord").get("lon").getAsFloat();
            lat = jsonObject.getAsJsonObject("coord").get("lat").getAsFloat();
            weather = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
            //System.out.println(array);
            temp = jsonObject.getAsJsonObject("main").get("temp").getAsFloat();
            feels_like = jsonObject.getAsJsonObject("main").get("feels_like").getAsFloat();
            temp_min = jsonObject.getAsJsonObject("main").get("temp_min").getAsFloat();
            temp_max = jsonObject.getAsJsonObject("main").get("temp_max").getAsFloat();
            pressure = jsonObject.getAsJsonObject("main").get("pressure").getAsFloat();
            humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsFloat();
            speed = jsonObject.getAsJsonObject("wind").get("speed").getAsFloat();
            //deg = jsonObject.getAsJsonObject("wind").get("deg").getAsFloat();
            //gust = jsonObject.getAsJsonObject("wind").get("gust").getAsFloat();
            Date date = Calendar.getInstance().getTime();
            weatherData.add(new WeatherData(result, lon, lat, weather, temp, feels_like, temp_min, temp_max, pressure, humidity, speed, location, date));
            currentWeather = weatherData.get(weatherData.size() - 1);
            //
            currentWeather.everything();
            //System.out.println("---------------------------------------------------------------------------");
            return false;
        } catch (Exception e) {
            System.out.println(e);
            errorLocation = location;
            errorCode = countryCode;
            return true;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

}
