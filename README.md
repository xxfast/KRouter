# <img src="https://kotlinlang.org/assets/images/favicon.svg" height="23"/> Router
[![Kotlin Experimental](https://kotl.in/badges/experimental.svg)](https://kotlinlang.org/docs/components-stability.html)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.8.0-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)

![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
![badge-desktop](http://img.shields.io/badge/platform-desktop-4D76CD.svg?style=flat)

An example [Compose Multiplatform](https://github.com/JetBrains/compose-jb) navigation library that use [Decompose](https://github.com/arkivanov/Decompose) and [Essenty](https://github.com/arkivanov/Essenty) to create an API inspired by [Conductor](https://github.com/bluelinelabs/Conductor)

A detailed breakdown available in this [Medium article](https://medium.com/@xxfast/diy-compose-multiplatform-navigation-with-decompose-94ac8126e6b5)

## Features
- 🚏 `Router`; A FILO stack for your screen configurations
- 📦 `ViewModel`; A instance that survives configuration changes and gets cleared when the user leaves the screen
- ☠️ `SavedState`; Restore your view models state after the process death.
- 🚉 Multiplatform!

## Usage

```kotlin
sealed class Screen: Parcelable {
  @Parcelize object List : Screen()
  @Parcelize data class Details(val detail: String) : Screen()
}

@Composable
fun ListDetailScreen() {
  val router: Router<Screen> = rememberRouter(listOf(List))

  RoutedContent(
    router = router,
    animation = stackAnimation(slide()),
  ) { screen ->
    when (screen) {
      List -> ListScreen(onSelect { detail -> router.push(detail) } )
      is Details -> DetailsScreen(screen.detail)
    }
  }
}

@Composable
fun ListScreen(onSelect: (detail: String) -> Unit) {
  val viewModel: ListViewModel = 
    rememberViewModel { savedState -> ListViewModel(savedState) }
  
  val state: ListState by viewModel.states.collectAsState()

  LazyColumn {
    items(state.items) { item ->
      TextButton(onClick = { onSelect(item) } ) {
        Text(text = item)
      }
    }
  }
}

@Composable
fun DetailScreen(detail: String) {
  val viewModel: ListViewModel = 
    rememberViewModel(key = detail) { DetailsViewModel(detail) }

  val state: DetailsState by viewModel.states.collectAsState()

  Toolbar(title = detail)
  Text(state.descriptions)
}
```

## Sample

Check out a sample news app in the `/sample` directory

https://user-images.githubusercontent.com/13775137/223930832-94a210f7-b477-4bcf-a40f-194ce15b41bd.mp4

https://user-images.githubusercontent.com/13775137/223930769-78c1a839-bddc-4379-8994-f8cb028e97e1.mp4


