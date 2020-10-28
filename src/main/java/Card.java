import java.util.Random;

public class Card {
    private int[] cardNumbers;

    private int id;
    private String cardNumber;
    private String pin;
    private double balance;

    public Card() {
        this.generateCard();
    }

    public Card(int id, String cardNumber, String pin, double balance) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public void generateCard() {

        cardNumber = this.generateCardNumber();
        pin = this.generatePin();
        balance = 0;
        id = this.generateId();

    }

    public String generateCardNumber(){
        //generation of the card number follows Luhn Algorithm
        // whole card number is 16-digit length
        Random random = new Random();
        cardNumbers = new int[15];
        StringBuilder sb = new StringBuilder(cardNumbers.length);

        //cardNumber begins with 4, IIN must be 400000
        cardNumbers[0] = 4;
        for (int i = 1; i <= 5; i++){
            cardNumbers[i] = 0;
        }

        // card number is 16-digit length, we start from 7th digit and end on 15th
        for(int i = 6; i <= 14; i++){
            cardNumbers[i] = random.nextInt(10);
        }

        for(int a: cardNumbers){
            sb.append(a);
        }

        for(int i = 0; i < 15; i += 2 ) {
            cardNumbers[i] = cardNumbers[i] * 2;

            if(cardNumbers[i] > 9) {
                cardNumbers[i] -= 9;
            }
        }

        //16th digit is a checksum - so the sum of all numbers is divisible by 10
        int sum = 0;
        for(int a: cardNumbers){
            sum += a;
        }
        if(!(10 - (sum % 10) == 10)) {
            sb.append(10 - (sum % 10));
        } else {
            sb.append(0);
        }

        return sb.toString().trim();
    }

    public String generatePin() {
        Random random = new Random();

        StringBuilder sb = new StringBuilder(4);
        for(int i = 0; i <4; i++){
            sb.append(random.nextInt(10));
        }
        return sb.toString().trim();
    }

    public int generateId() {
        return Integer.valueOf(this.cardNumber.substring(8,15));
    }

    public boolean LuhnCheck(String number){
        String[] numbers = number.split("(?!^)");
        int sum = 0;
        for(int i = 0; i < numbers.length; i++){
            if(i % 2 == 0 && i - 1 != numbers.length){
                if(Integer.valueOf(numbers[i]) * 2 > 9){
                    sum += Integer.valueOf(numbers[i]) * 2 - 9;
                } else {
                    sum += Integer.valueOf(numbers[i]) * 2;
                }

            } else {
                sum += Integer.valueOf(numbers[i]);
            }
        }

        if(sum % 10 == 0){
            return true;
        } else {
            return false;
        }
    }

    public String getPin(){
        return this.pin;
    }

    public double getBalance() {
        return this.balance;
    }

    public String getCardNumber(){
        return this.cardNumber;
    }

    public int getId(){
        return this.id;
    }
}
