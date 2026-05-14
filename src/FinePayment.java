public class FinePayment extends PaymentFramework {

    private String memberName;
    private String cardNumber;
    private String itemTitle;
    private int daysOverdue;

    public FinePayment(String memberName, String cardNumber, String itemTitle,
                       int daysOverdue, double ratePerDay, double discountRate) {
        super(daysOverdue * ratePerDay, discountRate);
        this.memberName = memberName;
        this.cardNumber = cardNumber;
        this.itemTitle = itemTitle;
        this.daysOverdue = daysOverdue;
    }

    @Override
    protected boolean isPaymentValid() {
        if (memberName == null || memberName.trim().isEmpty()) {
            System.out.println("  Invalid member name.");
            return false;
        }
        if (daysOverdue <= 0) {
            System.out.println("  No overdue fines found.");
            return false;
        }
        if (baseAmount <= 0) {
            System.out.println("  Invalid fine amount.");
            return false;
        }
        return true;
    }

    @Override
    public void processInvoice() {
        System.out.println("-------------------------------------");
        System.out.println("        FINE PAYMENT INVOICE         ");
        System.out.println("-------------------------------------");
        System.out.println("  Member  : " + memberName);
        System.out.println("  Card No : " + cardNumber);
        System.out.println("  Item    : " + itemTitle);
        System.out.println("  Overdue : " + daysOverdue + " day(s)");
        System.out.println("  Base Fine : PHP " + String.format("%.2f", baseAmount));
        System.out.println("  VAT (12%) : PHP " + String.format("%.2f", baseAmount * TAX_RATE));
        System.out.println("  Discount  : " + discountRate + "%");

        if (!isPaymentValid()) {
            System.out.println("  Invalid payment. Transaction cancelled.");
            return;
        }

        double amountDue = baseAmount;
        amountDue = computeTax(amountDue);
        amountDue = computeDiscount(amountDue);
        completeTransaction(amountDue);

        PaymentDAO.savePayment(cardNumber, "Fine",
            baseAmount, baseAmount * TAX_RATE, discountRate, amountDue);
        System.out.println("-------------------------------------");
    }
}