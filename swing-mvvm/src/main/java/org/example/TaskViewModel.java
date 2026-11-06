package org.example;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * Presentation state and actions for the task screen.
 * This class has no dependency on Swing.
 */
public final class TaskViewModel {
    public static final String PROP_TASKS = "tasks";
    public static final String PROP_MESSAGE = "message";

    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private final List<Task> tasks = new ArrayList<>();
    private String message = "Add a task to get started.";

    public List<Task> getTasks() {
        // return a copy so callers can't modify
        return List.copyOf(tasks);
    }

    public String getMessage() {
        return message;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changes.removePropertyChangeListener(listener);
    }

    public void addTask(String title) {
        String cleaned = title == null ? "" : title.trim();
        if (cleaned.isEmpty()) {
            setMessage("Please enter a task title.");
            return;
        }

        List<Task> oldTasks = getTasks();
        tasks.add(new Task(cleaned));
        changes.firePropertyChange(PROP_TASKS, oldTasks, getTasks());
        setMessage("Task added.");
    }

    public void completeTask(int index) {
        if (index < 0 || index >= tasks.size()) {
            setMessage("Select a task first.");
            return;
        }

        Task task = tasks.get(index);
        if (task.isCompleted()) {
            setMessage("That task is already completed.");
            return;
        }

        // catch b4 mutation. tasks are mutable, so lists are not immune either
        List<String> oldTasks = tasks.stream().map(Task::toString).toList();
        task.markCompleted();
        changes.firePropertyChange(PROP_TASKS, oldTasks, getTasks());
        setMessage("Task completed.");
    }

    private void setMessage(String newMessage) {
        String oldMessage = message;
        message = newMessage;
        changes.firePropertyChange(PROP_MESSAGE, oldMessage, newMessage);
    }
}
