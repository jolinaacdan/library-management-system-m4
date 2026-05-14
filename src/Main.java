import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseSetup.initialize();

        MemberDAO memberDAO = new MemberDAO();
        ItemDAO itemDAO = new ItemDAO();
        LoanDAO loanDAO = new LoanDAO();
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        do {
            printMenu();
            try {
                System.out.print("Enter choice: ");
                choice = Integer.parseInt(scanner.nextLine().trim());
                System.out.println();

                switch (choice) {
                    case 1 -> itemDAO.browseItems();
                    case 2 -> {
                        System.out.print("Enter keyword to search: ");
                        itemDAO.searchItems(scanner.nextLine());
                    }
                    case 3 -> {
                        System.out.print("Enter Library Card Number: ");
                        Member m = memberDAO.findMember(scanner.nextLine());
                        if (m == null) { System.out.println("  Member not found. Please register first."); break; }
                        itemDAO.browseItems();
                        System.out.print("Enter Item ID to borrow: ");
                        loanDAO.borrowItem(m, scanner.nextLine());
                    }
                    case 4 -> {
                        System.out.print("Enter Library Card Number: ");
                        Member m = memberDAO.findMember(scanner.nextLine());
                        if (m == null) { System.out.println("  Member not found."); break; }
                        System.out.print("Enter Item ID to return: ");
                        loanDAO.returnItem(m, scanner.nextLine());
                    }
                    case 5 -> {
                        System.out.print("Enter Full Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Contact Number: ");
                        String contact = scanner.nextLine();
                        System.out.print("Enter Address: ");
                        String address = scanner.nextLine();
                        memberDAO.registerMember(name, contact, address);
                    }
                    case 6 -> handleMembershipPayment(scanner);
                    case 7 -> handleFinePayment(scanner);
                    case 8 -> {
                        System.out.print("Enter Library Card Number: ");
                        PaymentDAO.showPaymentHistory(scanner.nextLine());
                    }
                    case 0 -> System.out.println("  Goodbye! Thank you for using Library System.");
                    default -> System.out.println("  Invalid choice. Please enter 0-8.");
                }

            } catch (NumberFormatException e) {
                System.out.println("  Invalid input! Please enter a number only.");
                choice = -1;
            } catch (Exception e) {
                System.out.println("  Unexpected error: " + e.getMessage());
                choice = -1;
            }

            if (choice != 0) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }

        } while (choice != 0);
        scanner.close();
    }

    static void printMenu() {
        System.out.println("=====================================");
        System.out.println("     LIBRARY MANAGEMENT SYSTEM      ");
        System.out.println("           MILESTONE 4              ");
        System.out.println("=====================================");
        System.out.println("  --- Library Features ---");
        System.out.println("  [1] Browse Catalog");
        System.out.println("  [2] Search Catalog");
        System.out.println("  [3] Borrow Item");
        System.out.println("  [4] Return Item");
        System.out.println("  [5] Register Member");
        System.out.println("  --- Payment Features ---");
        System.out.println("  [6] Pay Membership Fee");
        System.out.println("  [7] Pay Overdue Fine");
        System.out.println("  [8] View Payment History");
        System.out.println("  [0] Exit");
        System.out.println("=====================================");
    }

    static void handleMembershipPayment(Scanner scanner) {
        try {
            System.out.print("Enter member name: ");
            String name = scanner.nextLine();
            System.out.print("Enter library card number: ");
            String card = scanner.nextLine();
            System.out.print("Enter membership fee (PHP): ");
            double fee = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Enter discount rate (0 if none): ");
            double discount = Double.parseDouble(scanner.nextLine().trim());

            if (fee < 0 || discount < 0 || discount > 100)
                throw new IllegalArgumentException("Invalid fee or discount value.");

            MembershipPayment payment = new MembershipPayment(name, card, fee, discount);
            payment.processInvoice();

        } catch (NumberFormatException e) {
            System.out.println("  Error: Please enter valid numbers.");
        } catch (IllegalArgumentException e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    static void handleFinePayment(Scanner scanner) {
        try {
            System.out.print("Enter member name: ");
            String name = scanner.nextLine();
            System.out.print("Enter library card number: ");
            String card = scanner.nextLine();
            System.out.print("Enter item title: ");
            String title = scanner.nextLine();
            System.out.print("Enter number of days overdue: ");
            int days = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter fine rate per day (PHP): ");
            double rate = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Enter discount rate (0 if none): ");
            double discount = Double.parseDouble(scanner.nextLine().trim());

            if (days < 0 || rate < 0 || discount < 0 || discount > 100)
                throw new IllegalArgumentException("Invalid input values.");

            FinePayment payment = new FinePayment(name, card, title, days, rate, discount);
            payment.processInvoice();

        } catch (NumberFormatException e) {
            System.out.println("  Error: Please enter valid numbers.");
        } catch (IllegalArgumentException e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }
}