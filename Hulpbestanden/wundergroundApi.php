<?php
/**
 * Created by Tibo Vanheule.
 * Date: 4/04/2018
 * Time: 16:40
 * For the project Lessenrooster Ugent
 */
header('Content-Type:text/plain');
$key = null;
if(isset($_REQUEST['key'])){
    $key = $_REQUEST['key'];
}
$city = null;
if(isset($_REQUEST['city'])){
    $city = $_REQUEST['city'];
}
try {
    $json_string = file_get_contents("http://api.wunderground.com/api/" . $key . "/conditions/q/be/".$city.".json");
    $parsed_json = json_decode($json_string);
    $icon = $parsed_json->{'current_observation'}->{'icon'};
    $weather = $parsed_json->{'current_observation'}->{'weather'};
    $temp_c = $parsed_json->{'current_observation'}->{'temp_c'};
    $wind_speed = $parsed_json->{'current_observation'}->{'wind_kph'};
    $wind_degrees = $parsed_json->{'current_observation'}->{'wind_degrees'};
    $humidity = $parsed_json->{'current_observation'}->{'relative_humidity'};
    $location = $parsed_json->{'current_observation'}->{'display_location'}->{'city'};

    echo "$icon\n$weather\n$temp_c\n$wind_speed\n$wind_degrees\n$humidity\n$location";
}catch (Exception $error){
        echo "Could not get the weather data :(";
}