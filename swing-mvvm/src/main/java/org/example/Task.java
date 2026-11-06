package org.example;

import java.util.Objects;

/** Domain model: deliberately independent of Swing and presentation code. */
public final class Task {
    private final String title;
    private boolean completed;

    public Task(String title) {
        this.title = Objects.requireNonNull(title);
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        completed = true;
    }

    @Override
    public String toString() {
        return (completed ? "[x] " : "[ ] ") + title;
    }
}
