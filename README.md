# 🎬 Shutterfly Take-Home Project  
**By Etgar Perets**  

## 🏛️ Architecture  
This project is built using **MVVM** (Model-View-ViewModel) architecture with **Hilt** for Dependency Injection, ensuring a clean, maintainable, and testable codebase.  

### 📂 Project Structure  
```plaintext
com.example.myapplication
│
├─ model                # Data and business logic layer
│   ├─ ApiService.kt    # API calls with Retrofit
│   ├─ MovieRepository.kt # Data management and flow
│   ├─ models.kt        # Data models (Movie, Genre, MovieDetails)
│   └─ hilt_module.kt   # Hilt dependency injection module
│
├─ view                 # UI layer
│   ├─ movieDetails # Screen that showcase details about a movie
│   │   └─ movieDetailsScreen.kt
│   ├─ moviesByGenre  # Main screen that shows popular movies by genre
│   │   ├─ MoviesByGenreScreen.kt
│   │   └─ MoviePoster.kt
│   └─ MainActivity.kt # main activity with NavHost for navigation
│
└─ viewmodel            # ViewModel layer
    └─ MovieViewModel.kt
```
## Key Features
- **MVVM Architecture:** Ensures modularity and separation of concerns.  
- **Reactive UI with Jetpack Compose:** Modern, declarative UI design.  
- **Dependency Injection with Hilt:** Simplifies dependency management.  
- **Coroutines & Flow:** For efficient asynchronous data handling.  
- **Retrofit Integration:** Robust network operations with error handling.  

### 🎨 UI Highlights
- **Two-Screen Navigation:** Effortless transition between genre-based movie lists and detailed views.  
- **Material Design 3:** Consistent and visually appealing interface.  
- **Genre-Based Tabs:** Smooth scrolling and animated transitions.  
- **Dynamic Content Loading:** Efficient movie grid rendering with support for multiple pages.  
- **Interactive UI Elements:** Seamless navigation to movie details useing NavHost.  
- **Image Loading with Coil & Glide:** Fast and efficient with placeholders.  
- **Subtle Animations:** Enhances user experience with polished transitions.  
