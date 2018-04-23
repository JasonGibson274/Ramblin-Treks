package pathing_svc;

public class SchedulerTask implements Runnable {
    private final PathingService pathingService;

    public SchedulerTask(PathingService pathingService) {
        this.pathingService = pathingService;
    }

    @Override
    public void run() {
        pathingService.cleanUp();
        System.out.println("job executed");
    }
}
