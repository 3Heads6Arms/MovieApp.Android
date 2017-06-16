Api key is required to run this application and can be acquired from https://www.themoviedb.org/.

The Api key should be added to gradle.properties as follow:
MovieApiKey ="[YOUR_MOVIE_DB_API_KEY]"

Api key from Youtube Api is required to run this application, can be acquired from https://console.developers.google.com/
The YouTube api key should be added to gradle.properties as follow:
YouTubeApiKey = "[YOUR_YOUTUBE_API_KEY]"

Also YouTube Api for android is required
Either download .jar file from https://developers.google.com/youtube/android/player/downloads/, and add it to ./app/libs/ folder.
Or add it from Maven, if possible (please tell me how, maven doesn't seem to find it, and some package code written from hand causes errors);
