import java.util.*;

class Logic
{
    private static final int N = 5;
//    private static Collection<Money> collection;


    static Money getMin(Map<User,Money> map) {
        Map.Entry<User, Money> min = Collections.min(map.entrySet(), (new ComparatorForMoney()));
        return min.getValue();
    }

    static Money getMax(Map<User,Money> map)
    {
        Map.Entry<User, Money> max = Collections.max(map.entrySet(), (new ComparatorForMoney()));
        return max.getValue();
    }

    public static void main (String[] args)
    {
        Map<User,Money> persons = new HashMap<>();
        persons.put(new User("Vasya","vasya@gmail.com"),new Money(214.23));
        persons.put(new User("Vlad","petya@gmail.com"),new Money(13.11));
        persons.put(new User("Kiril","kiril@gmail.com"),new Money(55.22));
        persons.put(new User("Stas","stas@gmail.com"),new Money(153.21));
        persons.put(new User("Yara", "sdsd@dsds"), new Money(150d));
        double equality = equality(persons);
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 0; i < N-1; i++) {
            transactions.add(calculateCompensation(persons, equality));
        }

        for (Transaction print: transactions ) {
            System.out.println(print.toString());
        }

        new AddResultToFile(transactions,"C:/games/result.txt");
    }

    public static Transaction calculateCompensation (Map<User, Money> contributions, double equality) {
        Transaction transaction = null;

        Money moneyMax = getMax(contributions);
        Money moneyMin = getMin(contributions);
        User userWithCredit = null , userWithDebit = null;

        for (Map.Entry<User, Money> pair : contributions.entrySet()) {
            if (moneyMax.equals(pair.getValue())) {
                userWithCredit = pair.getKey();
            } else if (moneyMin.equals(pair.getValue())) {
                userWithDebit = pair.getKey();
            }
        }
            if(equality - moneyMin.getAmount() <
                    moneyMax.getAmount() - equality){
                moneyMax = new Money(moneyMax.getAmount() - equality + moneyMin.getAmount());
                transaction = new Transaction(userWithDebit,userWithCredit,equality - moneyMin.getAmount());
                moneyMin = new Money(equality);
                } else {
                moneyMin = new Money(moneyMin.getAmount() + moneyMax.getAmount() - equality);
                transaction = new Transaction(userWithDebit,userWithCredit,moneyMax.getAmount() - equality);
                moneyMax = new Money(equality);
            }

        contributions.put(userWithDebit, moneyMin);
        contributions.put(userWithCredit, moneyMax);
        return transaction;
    }

    static double equality(Map<User,Money> map){
        double result = 0;
        for (Money money: map.values()) {
            result += money.getAmount();
        }
        return result/N;
    }

}