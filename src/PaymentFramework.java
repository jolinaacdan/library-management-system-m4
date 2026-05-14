public abstract class PaymentFramework {

    protected double baseAmount;
    protected double discountRate;
    protected final double TAX_RATE = 0.12;

    public PaymentFramework(double baseAmount, double discountRate) {
        this.baseAmount = baseAmount;
        this.discountRate = discountRate;
    }

    // Abstract method — subclasses must implement this
    protected abstract boolean isPaymentValid();

    // Apply 12% VAT
    protected double computeTax(double amount) {
        double tax = amount * TAX_RATE;
        return amount + tax;
    }

    // Apply discount
    protected double computeDiscount(double amount) {
        double deduction = amount * (discountRate / 100);
        return amount - deduction;
    }

    // Final step — show confirmation
    protected void completeTransaction(double amountDue) {
        System.out.println("  Payment processed successfully.");
        System.out.println("  Total Amount Due: PHP " + String.format("%.2f", amountDue));
    }

    // Template method — coordinates everything
    public void processInvoice() {
        if (!isPaymentValid()) {
            System.out.println("  Invalid payment. Transaction cancelled.");
            return;
        }
        double amountDue = baseAmount;
        amountDue = computeTax(amountDue);
        amountDue = computeDiscount(amountDue);
        completeTransaction(amountDue);
    }
}