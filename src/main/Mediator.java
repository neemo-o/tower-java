package main;
// interface do carinha que comunica os componentes entre eles. as classes sao cegas sem o mediador presente
public interface Mediator {
    void notify(Object sender, String event);
}
