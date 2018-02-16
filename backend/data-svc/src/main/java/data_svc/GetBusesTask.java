package data_svc;

public class GetBusesTask implements Runnable {
    private DataService dataService;

    public GetBusesTask(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public void run() {
        dataService.grabAllBusPositions();
    }
}
