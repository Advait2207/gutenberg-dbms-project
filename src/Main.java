import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nMain Menu");
            System.out.println("1. Editing & Publishing");
            System.out.println("2. Production");
            System.out.println("3. Distribution");
            System.out.println("4. Reports");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    EditingService.menu();
                    break;
                case 2:
                    ProductionService.menu();
                    break;
                case 3:
                    DistributionService.menu();
                    break;
                case 4:
                    ReportService.menu();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }

        scanner.close();
    }
}