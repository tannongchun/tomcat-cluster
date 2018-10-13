package com.orangefunction.tomcat.redissessions;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;

public final class LifecycleSupport{
  // ----------------------------------------------------------- Constructors


  /**
   * Construct a new LifecycleSupport object associated with the specified
   * Lifecycle component.
   *
   * @param lifecycle The Lifecycle component that will be the source
   *  of events that we fire
   */
  public LifecycleSupport(Lifecycle lifecycle) {

    super();
    this.lifecycle = lifecycle;

  }


  // ----------------------------------------------------- Instance Variables


  /**
   * The source component for lifecycle events that we will fire.
   */
  private Lifecycle lifecycle = null;


  /**
   * The set of registered LifecycleListeners for event notifications.
   */
  private LifecycleListener listeners[] = new LifecycleListener[0];

  private final Object listenersLock = new Object(); // Lock object for changes to listeners


  // --------------------------------------------------------- Public Methods


  /**
   * Add a lifecycle event listener to this component.
   *
   * @param listener The listener to add
   */
  public void addLifecycleListener(LifecycleListener listener) {

    synchronized (listenersLock) {
      LifecycleListener results[] =
          new LifecycleListener[listeners.length + 1];
      System.arraycopy(listeners, 0, results, 0, listeners.length);
      results[listeners.length] = listener;
      listeners = results;
    }

  }


  /**
   * Get the lifecycle listeners associated with this lifecycle. If this
   * Lifecycle has no listeners registered, a zero-length array is returned.
   */
  public LifecycleListener[] findLifecycleListeners() {

    return listeners;

  }


  /**
   * Notify all lifecycle event listeners that a particular event has
   * occurred for this Container.  The default implementation performs
   * this notification synchronously using the calling thread.
   *
   * @param type Event type
   * @param data Event data
   */
  public void fireLifecycleEvent(String type, Object data) {

    LifecycleEvent event = new LifecycleEvent(lifecycle, type, data);
    LifecycleListener interested[] = listeners;
    for (int i = 0; i < interested.length; i++)
      interested[i].lifecycleEvent(event);

  }


  /**
   * Remove a lifecycle event listener from this component.
   *
   * @param listener The listener to remove
   */
  public void removeLifecycleListener(LifecycleListener listener) {

    synchronized (listenersLock) {
      int n = -1;
      for (int i = 0; i < listeners.length; i++) {
        if (listeners[i] == listener) {
          n = i;
          break;
        }
      }
      if (n < 0) {
        return;
      }
      LifecycleListener results[] =
          new LifecycleListener[listeners.length - 1];
      int j = 0;
      for (int i = 0; i < listeners.length; i++) {
        if (i != n)
          results[j++] = listeners[i];
      }
      listeners = results;
    }

  }


}
