package org.example;

import javax.swing.SwingUtilities;

public final class App {
    private App() {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaskViewModel viewModel = new TaskViewModel();
            TaskView view = new TaskView(viewModel);
            view.setVisible(true);
        });
    }
}
