package org.ter.container.pipeline;

import org.ter.container.*;
import org.ter.exception.LifecycleException;
import org.ter.lifecycle.Lifecycle;
import org.ter.lifecycle.LifecycleBase;
import org.ter.lifecycle.LifecycleState;

import java.util.ArrayList;
import java.util.Objects;

public class StandardPipeline extends LifecycleBase implements Pipeline {
    private Valve first;
    private Container container;
    private Valve basic;
    public StandardPipeline(Container container) {
        setContainer(container);
    }

    @Override
    public Valve getBasic() {
        return basic;
    }

    @Override
    public void setBasic(Valve valve) {

        // Change components if necessary
        Valve oldBasic = this.basic;
        if (oldBasic == valve) {
            return;
        }

        // Stop the old component if necessary
        if (oldBasic != null) {
            if (getLifecycleState().isAvailable() && (oldBasic instanceof Lifecycle)) {
                try {
                    ((Lifecycle) oldBasic).stop();
                } catch (LifecycleException exception) {
                   throw new RuntimeException(exception);
                }
            }
            if (oldBasic instanceof Contained) {
                try {
                    ((Contained) oldBasic).setContainer(null);
                } catch (Throwable t) {
                    throw new RuntimeException(new Exception(t));
                }
            }
        }

        // Start the new component if necessary
        if (valve == null) {
            return;
        }
        if (valve instanceof Contained) {
            ((Contained) valve).setContainer(this.container);
        }
        if (getLifecycleState().isAvailable() && valve instanceof Lifecycle) {
            try {
                ((Lifecycle) valve).start();
            } catch (LifecycleException e) {
                return;
            }
        }

        // Update the pipeline
        Valve current = first;
        while (current != null) {
            if (current.getNext() == oldBasic) {
                current.setNext(valve);
                break;
            }
            current = current.getNext();
        }

        this.basic = valve;
    }

    @Override
    public void addValve(Valve valve) {
        if(valve instanceof Contained){
            ((Contained) valve).setContainer(this.container);
        }
        if(getLifecycleState().isAvailable()){
            try {
                ((Lifecycle) valve).start();
            }catch (LifecycleException exception){
                throw new RuntimeException(exception);
            }
        }
        if(Objects.isNull(first)){
            first = valve;
            valve.setNext(basic);
        }else{
            Valve current = first;
            while (current != null) {
                if (current.getNext() == basic) {
                    current.setNext(valve);
                    valve.setNext(basic);
                    break;
                }
                current = current.getNext();
            }
        }
        container.fireContainerEvent(Container.ADD_VALVE_EVENT, valve);
    }

    @Override
    public Valve[] getValves() {
        Valve current = first;
        if(first == null){
            current = basic;
        }
        ArrayList<Valve> valves = new ArrayList<>();
        while (Objects.nonNull(current)){
            valves.add(current);
            current = current.getNext();
        }
        return valves.toArray(new Valve[0]);
    }

    @Override
    public void remoteValve(Valve valve) {
        Valve current = null;
        if(valve == first){
            first =  first.getNext();
        }else{
            current = first;
        }
        while (Objects.nonNull(current)){
            if(current == valve){
                current.setNext(valve.getNext());
                break;
            }
            current = current.getNext();
        }

        if(first == basic){
            first = null;
        }
        if(valve instanceof Contained){
            ((Contained) valve).setContainer(null);
        }
        if(valve instanceof Lifecycle){
            if(getLifecycleState().isAvailable()){
                try {
                    ((Lifecycle)valve).stop();
                } catch (LifecycleException exception) {
                    throw new RuntimeException(exception);
                }
            }
            try {
                ((Lifecycle)valve).destroy();
            }catch (LifecycleException exception){
                throw new RuntimeException(exception);
            }
        }
        container.fireContainerEvent(Container.REMOVE_CHILD_EVENT, valve);
    }

    @Override
    public Valve getFirst() {
        if(Objects.nonNull(first)){
            return first;
        }
        return basic;
    }

    @Override
    protected void initInternal() throws LifecycleException {
        //NOOP
    }

    @Override
    protected void startInternal() throws LifecycleException {
        setLifecycleState(LifecycleState.STARTING);
        Valve current = first;
        if(Objects.isNull(current)){
            current = basic;
        }
        while (Objects.nonNull(current)){
            if(current instanceof  Lifecycle){
                ((Lifecycle) current).start();
            }
            current = current.getNext();
        }
    }

    @Override
    protected void stopInternal() throws LifecycleException {
        setLifecycleState(LifecycleState.STOPPING);
        Valve current = first;
        if(Objects.isNull(current)){
            current = basic;
        }
        while (Objects.nonNull(current)){
            if(current instanceof  Lifecycle){
                ((Lifecycle) current).stop();
            }
            current = current.getNext();
        }
    }

    @Override
    protected void destroyInternal() throws LifecycleException {
        Valve[] valves = getValves();
        for (Valve valve : valves){
            remoteValve(valve);
        }
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public Container getContainer() {
        return container;
    }
    @Override
    public boolean findValve(String className){
        Valve[] valves = getValves();
        for (Valve valve : valves){
            if(className.equals(valve.getClass().getName())){
                return true;
            }
        }
        return false;
    }
}
