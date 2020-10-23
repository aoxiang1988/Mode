package com.application.selfdesignpattern.observer;

public interface SubjectInterface {
    void add(ObserverInterface observer);
    void notifyObserver();
    void remove(ObserverInterface observer);
}
