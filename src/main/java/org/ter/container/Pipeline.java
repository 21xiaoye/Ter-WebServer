package org.ter.container;

public interface Pipeline {
    Valve getBasic();
    void setBasic(Valve valve);
    void addValve(Valve valve);
    Valve[] getValves();
    void remoteValve(Valve valve);
    Valve getFirst();
    boolean findValve(String className);
}
