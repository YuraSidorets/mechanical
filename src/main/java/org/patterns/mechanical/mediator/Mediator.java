package org.patterns.mechanical.mediator;

public interface Mediator {

    void deleteNote();

    void saveChanges();
    void markNote();
    void clear();

    void registerComponent(Component component);
    void hideElements(boolean flag);
    void createGUI();
}
