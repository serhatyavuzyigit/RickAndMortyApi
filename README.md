# Rick And Morty App

## Architecture : MVVM


![alt text](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

### Model<br/>
This holds the data of the application. It cannot directly talk to the View. Generally, it’s recommended to expose the data to the ViewModel through Observables.<br/>
### View<br/>
It represents the UI of the application devoid of any Application Logic. It observes the ViewModel.<br/>
### ViewModel<br/>
It acts as a link between the Model and the View. It’s responsible for transforming the data from the Model. It provides data streams to the View. It also uses hooks or callbacks to update the View. It’ll ask for the data from the Model.

## Used Libraries
### recyclerview
### cardview
### retrofit
### rxjava
### glide
### paging
