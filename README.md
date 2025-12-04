# Modèle – Document d'explication

---

## Architecture

L’application est structurée suivant une approche Clean Architecture :  
- Couche *data* avec `HttpClothingService` (appel HTTP simple sur le JSON hébergé), `ClothingRemoteDataSource` et `ClothingLocalDataSource` pour le cache en mémoire (notes, favoris, partages).  
- Couche *domain* avec les modèles métiers, un `ClothingRepository` et des use cases dédiés (`GetClothesUseCase`, `GetClothingDetailsUseCase`, `SaveRatingUseCase`, `ToggleFavoriteUseCase`, `RegisterShareUseCase`) pour limiter les dépendances de la couche UI.  
- Couche *presentation* en Jetpack Compose + ViewModel : `HomeViewModel` et `DetailsViewModel` exposent des `StateFlow` consommés par les écrans Compose.  
Un petit service locator (`AppContainer`) instancie les dépendances afin de garder le projet auto‑contenu sans framework DI.

### Clean Architecture / MVVM / MVC ?
Clean Architecture avec un pattern MVVM pour la couche présentation (ViewModel + UI Compose).

### Diagramme d’architecture de l’application
Collez ici un diagramme réalisé par vos soins pour expliquer les différentes couches de votre application et comment elles interagissent entre elles pour gérer les données et l’interface utilisateur.

### Stack technique
- Kotlin + Coroutines / Flow pour le langage et l’asynchronisme.  
- Jetpack Compose + Material3 pour l’UI réactive.  
- Navigation Compose pour le routage téléphone/tablette.  
- Compose Window Size Classes pour adapter les layouts aux largeurs.  
- Coil pour le chargement d’images réseau.  
- Implémentation HTTP maison (HttpURLConnection + org.json) pour éviter une dépendance Retrofit.

### API
`HttpClothingService` appelle en GET un JSON statique hébergé sur GitHub, parse la réponse avec `JSONArray`/`JSONObject` et renvoie des DTO. Le `ClothingRepositoryImpl` déclenche le refresh au démarrage, met en cache les DTO et les transforme en modèles de domaine en agrégeant les données locales (avis, favoris, partages). Les use cases orchestrent les interactions depuis la couche présentation.

---

## Responsivité tablette

La largeur d’écran est lue via `WindowSizeClass` et la configuration Compose. En largeur compacte/moyenne (téléphone), on affiche une navigation classique `NavHost` avec un écran liste et un écran détails. En largeur étendue (tablette), `TabletHomeWithDetails` place la liste à gauche et un panneau de détails animé à droite (slide + fade) sur la même hiérarchie. Les composants principaux (`HomeScreen`, `DetailsScreen`, `CategorySection`, `ProductCard`) sont réutilisés tels quels sur les deux formats, avec seulement des ajustements de colonnes ou de paddings. Les grilles/colonnes se recalculent selon l’orientation et la largeur pour conserver des cartes lisibles.

### Maquettes de l’application

Vous pouvez insérer ici les maquettes fournies par votre designer pour ensuite comparer les vôtres dans les prochaines sections avec le travail demandé.

### Captures d’écran

#### Téléphone 6”
Insérez ici des captures de l’application sur téléphone.

#### Tablette 10”
Insérez ici des captures de l’application sur tablette.

---

## Accessibilité

### Accessibilité des images
Toutes les images chargées avec Coil (`AsyncImage`) possèdent un `contentDescription` contextualisé avec le nom du produit. Les visuels dans les écrans de détails et de listes annoncent clairement l’élément affiché, et les icônes décoratives gardent un `contentDescription` à `null` pour éviter les doublons vocaux.

### Accessibilité des zones de clic
Les boutons et surfaces tactiles utilisent `minimumInteractiveComponentSize` ou des paddings généreux pour atteindre le seuil de 48dp conseillé. Les actions essentielles (partage, favoris, retour) déclarent un rôle `Button` et une description d’état (`stateDescription`) quand pertinent. Les titres de section sont marqués comme `heading` pour faciliter la navigation avec les lecteurs d’écran, et les indicateurs de chargement possèdent une description explicite.

### Tests avec l’application Accessibility Scanner
Insérez ici des captures de l’application “Accessibility Scanner” sur téléphone et sur tablette pour montrer qu’aucun problème d’accessibilité n’a été détecté.
