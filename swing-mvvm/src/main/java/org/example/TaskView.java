package org.example;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public final class TaskView extends JFrame {
    private final TaskViewModel viewModel;
    private final DefaultListModel<Task> listModel = new DefaultListModel<>();
    private final JList<Task> taskList = new JList<>(listModel);
    private final JTextField titleField = new JTextField(22);
    private final JLabel messageLabel = new JLabel();

    private final PropertyChangeListener viewModelListener = this::onViewModelChanged;

    public TaskView(TaskViewModel viewModel) {
        super("Task Manager - Swing MVVM");
        this.viewModel = viewModel;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(440, 320));
        setLocationRelativeTo(null);

        buildUi();
        bindEvents();
        viewModel.addPropertyChangeListener(viewModelListener);
        renderTasks();
        messageLabel.setText(viewModel.getMessage());
    }

    private void buildUi() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel inputPanel = new JPanel(new BorderLayout(8, 0));
        inputPanel.add(titleField, BorderLayout.CENTER);

        JButton addButton = new JButton("Add task");
        addButton.addActionListener(event -> addTask());
        inputPanel.add(addButton, BorderLayout.EAST);

        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(taskList);

        JButton completeButton = new JButton("Mark completed");
        completeButton.addActionListener(event ->
                viewModel.completeTask(taskList.getSelectedIndex()));

        JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel.add(completeButton, BorderLayout.WEST);
        bottomPanel.add(messageLabel, BorderLayout.CENTER);

        root.add(inputPanel, BorderLayout.NORTH);
        root.add(scrollPane, BorderLayout.CENTER);
        root.add(bottomPanel, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private void bindEvents() {
        titleField.addActionListener(event -> addTask());
    }

    private void addTask() {
        viewModel.addTask(titleField.getText());
        titleField.setText("");
        titleField.requestFocusInWindow();
    }

    private void onViewModelChanged(PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
            case TaskViewModel.PROP_TASKS -> renderTasks();
            case TaskViewModel.PROP_MESSAGE ->
                    messageLabel.setText(viewModel.getMessage());
            default -> {
                // ignore stuff this view does not display
            }
        }
    }

    private void renderTasks() {
        listModel.clear();
        for (Task task : viewModel.getTasks()) {
            listModel.addElement(task);
        }
    }
}
