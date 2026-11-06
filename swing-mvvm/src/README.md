# Swing MVVM Sample

A tiny Java Swing task manager demonstrating Model-View-ViewModel (MVVM).

## Run
```bash
mvn clean package
java -cp target/classes com.example.mvvm.App
```

## Structure
- `Task`  is plain domain model; knows nothing about Swing.
- `TaskViewModel`  owns UI-facing state and application actions. It exposes changes through `PropertyChangeSupport` and has no Swing dependency.
- `TaskView`  is the Swing UI. It forwards user actions to the ViewModel and renders state received from it.
- `App`  creates and connects the objects.

## MVVM notes
- The View never edits the task collection directly
- The ViewModel exposes read-only snapshots to callers
- `PropertyChangeSupport` is a lightweight observable-state mechanism suitable for small Swing applications.
- Swing UI updates happen on the Event Dispatch Thread (EDT). The ViewModel methods in this sample are called from Swing event handlers, so notifications are delivered on the EDT.
- For larger applications, consider separating repositories/services from the ViewModel, using immutable view-state objects, and moving long-running work off the EDT (then marshal state updates back onto the EDT).
