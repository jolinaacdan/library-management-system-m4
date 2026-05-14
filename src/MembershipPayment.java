public class MembershipPayment extends PaymentFramework {

    private String memberName;
    private String cardNumber;

    public MembershipPayment(String memberName, String cardNumber,
                              double fee, double discountRate) {
        super(fee, discountRate);
        this.memberName = memberName;
        this.cardNumber = cardNumber;
    }

    @Override
    protected boolean isPaymentValid() {
        if (memberName == null || memberName.trim().isEmpty()) {
            System.out.println("  Invalid member name.");
            return false;
        }
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            System.out.println("  Invalid card number.");
            return false;
        }
        if (baseAmount <= 0) {
            System.out.println("  Invalid membership fee.");
            return false;
        }
        return true;
    }

    @Override
    public void processInvoice() {
        System.out.println("-------------------------------------");
        System.out.println("     MEMBERSHIP PAYMENT INVOICE      ");
        System.out.println("-------------------------------------");
        System.out.println("  Member : " + memberName);
        System.out.println("  Card No: " + cardNumber);
        System.out.println("  Base Fee     : PHP " + String.format("%.2f", baseAmount));
        System.out.println("  VAT (12%)    : PHP " + String.format("%.2f", baseAmount * TAX_RATE));
        System.out.println("  Discount     : " + discountRate + "%");
        super.processInvoice();
        System.out.println("-------------------------------------");
    }
}