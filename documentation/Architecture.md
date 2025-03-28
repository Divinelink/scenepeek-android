# App Architecture

ScenePeek follows a MVVM architecture pattern throughout the project. Below is the architecture diagram for the Login Screen, though every screen in the app should follow the same organization.  

For the most up to date diagram, check [Miro](https://miro.com/app/board/uXjVPtAQGyM=/).

![Architecture Diagram](assets/ScenePeekFlows.png)

This document is intended to clarify each of the components in the above image and their responsibilities. 

## Repository

A repository is any component that is responsible for making data requests. This could be to a remote server, local preferences, database, etc. The repository should only request and receive data. It should not have any side effects such as calling a different repository. The repository should be responsible for mapping information from DTO (data transfer object) models to domain specific data classes.

Repositories should not be responsible for any data manipulation of a response. For example, if a repository is requesting a list of users, but a screen only cares about users with a certain property, the use case should be responsible for filtering the list accordingly. 

## Use Case

The purpose of a use case is to perform any specific action that occurs on a screen. This could be fetching or submitting data, filtering items, etc. Domain specific business logic like this deserves its own component in the application's architecture.

A repository is only responsible for requesting and receiving data. A ViewModel is only responsible for taking the results of an action, and mapping it into a ViewState. The use case is responsible for whatever happens in between - consuming responses from data requests, and mapping that into a relevant result for the ViewModel to handle.

## ViewModel

This component is responsible for connecting a screen with any relevant use cases. The ViewModel consumes UI events, triggers a necessary use case, and processes a use case response into a ViewState that is exposed for the screen to render.

A ViewModel may also have certain actions that are triggered the moment ViewModel is created.

## View

The View component is solely responsible for being able to display data on the UI. It can consume a view state which specifies what information should be rendered. Any UI events such as inputs, button clicks, gestures, etc will be passed to the ViewModel which determines what action needs to happen. 

If a screen has multiple complex views or multiple responsibilities, it's possible to reference multiple
ViewModel dependencies.

