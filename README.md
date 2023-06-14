### Hacker News Api Serves:
* To use this application, please download the docker first.
* download the docker-compose.yaml file to a local system.
* run the command `docker-compose up`, you should be in the directory where docker-compose.yaml is downloaded.
* Once the application starts, then three endpoints are exposed:
  * `http://localhost:9090/api/v1/stories/top-stories`: will return the top ten stories on hacker news api.
  * `http://localhost:9090/api/v1/stories/past-stories`: will return the stories that were served in last api call.
  * `http://localhost:9090/api/v1/stories/comments/{storyId}`: will return the top ten comments on the story with storyId, sorted by total number of comments.