package org.patterns.mechanical.mediator;

public interface Component {
    void setMediator(Mediator mediator);
    String getName();
}
