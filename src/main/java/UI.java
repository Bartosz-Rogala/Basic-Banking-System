import java.util.Scanner;

public class UI {
    Scanner scan = new Scanner(System.in);
    Card newCard;

    public UI(){

    }

    public void startUI(Database newDatabase){

        while(true){
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            System.out.print(">");


            String input = scan.nextLine();

            if (input.equals("1")) {
                newCard = new Card();
                newDatabase.input(newCard.getId(), newCard.getCardNumber(), newCard.getPin(), newCard.getBalance());
                System.out.println("Your card has been created");
                System.out.println("Your card number:");
                System.out.println(newCard.getCardNumber());
                System.out.println("Your card PIN:");
                System.out.println(newCard.getPin());
            } else if (input.equals("2")) {
                System.out.println("Enter your card number:");
                System.out.print(">");
                String input2 = scan.nextLine();
                System.out.println("Enter your PIN:");
                System.out.print(">");
                String input3 = scan.nextLine();



                if(newDatabase.logIntoAccount(input2,input3)){
                    System.out.println("You have successfully logged in!");
                    System.out.println("");
                    newCard = newDatabase.read(input2);

                    while(true) {
                        System.out.println("1. Balance");
                        System.out.println("2. Add income");
                        System.out.println("3. Do transfer");
                        System.out.println("4. Close account");
                        System.out.println("5. Log out");
                        System.out.println("0. Exit");
                        System.out.print(">");
                        input = scan.nextLine();
                        if (input.equals("1")) {
                            System.out.println("Balance: " + newDatabase.read(input2).getBalance());
                        } else if (input.equals("2")) {
                            System.out.println("How much money would you like to deposit?");
                            double amount = Double.valueOf(scan.nextLine());
                            if (newDatabase.update(newCard.getId(), amount)) {
                                System.out.println("Money deposited successfully.");
                            } else {
                                System.out.println("Something went wrong, please try again");
                            }

                        } else if (input.equals("3")) {
                            System.out.println("Transfer");
                            System.out.println("Enter card number:");
                            String cardNumber = scan.nextLine();
                            if(newCard.LuhnCheck(cardNumber)) {
                                if(newDatabase.ifExists(cardNumber)) {
                                    System.out.println("Enter how much money you want to transfer:");
                                    double moneyAmount = Double.valueOf(scan.nextLine());
                                    if (newDatabase.read(input2).getBalance() >= moneyAmount) {
                                        newDatabase.transfer(input2, cardNumber, moneyAmount);
                                        System.out.println("Success!");
                                    } else {
                                        System.out.println("Not enough money!");
                                    }
                                } else {
                                    System.out.println("Such a card does not exist.");
                                }
                            } else {
                                System.out.println("Probably you made mistake in the card number. Please try again!");
                            }



                        } else if (input.equals("4")) {
                            newDatabase.delete(input2);
                            System.out.println("The account has been closed!");
                            break;
                        } else if (input.equals("5")) {
                            System.out.println("You have successfully logged out!");
                            break;
                        } else if (input.equals("0")) {
                            System.out.println("Bye!");
                            return;
                        }
                    }



                } else {
                    System.out.println("Wrong card number or PIN!");
                }
            } else if (input.equals("0")) {
                System.out.println("Bye!");
                break;
            }
        }
    }
}
