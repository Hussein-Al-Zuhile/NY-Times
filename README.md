# NY Times
**NY Times** is an app built using android native platform to show the most viewed topics in NY Times.

## Clean architecture and MVVM 🏗
The app follows the recommended architecture from google, there is three layers:
- **Data:** contains the remote data source and repository implementation, and its DI Module (i.e DataModule).
  -The remote data source responsible for providing retrofit remote service.
  -The remote service responsible for fetching the data.
  -The repository responsible for converting this data into a *State* and deliver it to the use case **without going through any logic processing.**
- **Domain:** contains the use-cases and the state implementation.
  -The state is a Sealed class that represents the current state.
  -A use-case **must handle any business logic** before or after calling the repository ( like check the internet or input parameters validity *"we didn't have this situation"* ).
- **Presentation:** contains the view models and the UI, the state should be remain in the view model to survive configuration changes and going to the background.

## Gradle versioning ⚙️
To version the libraries and anything in the gradle files from a centralized place, we created ***versions.gradle*** file which make it **easy to follow up** with your libraries and variables versions.

## Theming 🎨
The app follows the best practices in theming throughout these points:
- **No Static:** almost every text style and color and shape is coming from *MaterialTheme* object, and that's will provide dynamic changes if there is multiple themes in our app or *if ( dynamic or dark/light theme changes happened )* , no hard coded values. and that's what google recommend.
- **Dynamic colors (Android 12+):** the app uses the material 3 dynamic colors which provide very beautiful color theming integration with the whole android system theme.
- **Dark theme**: supports dark theme for material3 and material2 versions.
- **Surfaces:** the screens uses Surface as a root for its composable tree, and that will provide a perfect content color for inner composable nodes.

## UI decoupling and Previewing 👓
The preview and UI had some best practices, we created a preview annotation for light and dark themes, and **we passed a flow or a state to the screens instead of passing the view model, that's will isolate the screen from view model so we can 1-Preview, 2-reuse and 3-test it very easily.**

## Unit test 🧪
*( There is too little benefit of building tests for happy paths )*, so I focused on building some very edge cases in the get topics use case, I tested the repository and the use case together which increases the code coverage ( So it's actually integration test ) and mocked the remote service and connection manager to get some edge cases.
The app should contain tests for UI, and that's what I wanted to do but there is an issue in Compose testing in the version that I've used, so I skipped it.
The UI test that I wanted to build is testing that the loading state showing the progress indicator, and failure state showing the snackbar, and success showing the same items in the state.

## Conclusion ✔️
I am a Senior android developer with more than 5 years experience who seeks the latest best practices, features, tools and articles about Native android development, and I am happy to be a part of something big like your institution and hearing from you what do you think about me ❤️.
[Linkedin account](https://www.linkedin.com/in/hussein-al-zuhile-7026011a5/)
