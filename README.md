### Weather Feed

The Weather Feed application uses a single-activity architecture and MVVM

Libraries used include Retrofit, Hilt, Coil, Gradle Secrets, and Mockito. 

To build, retrieve a valid API key in yor  for OpenWeather's onecall API 3.0 (https://openweathermap.org/api/one-call-3)
or reach out, and I can provide one.

You will need to specify that in your local.properties file like

```
OPEN_WEATHER_API_KEY={api_key_value}
```

There is also a LocalTestRepository available for mocking valid responses.

Unit tests can be found in the unit test package. 

