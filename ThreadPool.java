import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPool {
  private int size = 4;
  private Queue<Runnable> tasks = new LinkedList<Runnable>();
  private final ReentrantLock lock = new ReentrantLock();

  public ThreadPool() {
    this.initiateThreadPool();
  }

  private void log(final String str) {
    System.out.println(str);
  }

  private void initiateThreadPool() {
    for (int i = 0; i < size; i++) {
      String name = "thread-" + i;
      log(name + " created!");
      Thread worker = new Thread(() -> {
        while (true) {
          Runnable task = getTask();
          if (task != null) {
            log("EXECUTED by: " + name + "\n");
            task.run();
          } else {
            log("---");
          }
        }
      }, name);
      worker.start();
    }
  }

  private void initiateSingleThreadPool() {

    Thread worker = new Thread(() -> {
      while (true) {
        if (!tasks.isEmpty()) {
          Runnable task = tasks.poll();
          if (task != null)
            task.run();
        }
      }
    }, "Main Thread");
    worker.start();
    log("Main Thread created!");

  }

  private Runnable getTask() {
    lock.lock();
    try {
      return tasks.poll();
    } finally {
      lock.unlock();
    }
  }

  public void execute(Runnable task) {
    log("Task is added to the list");
    tasks.add(task);
  }

}
