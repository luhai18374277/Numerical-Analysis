import java.util.Scanner;
import java.util.Vector;

public class Waiter extends Person{
    Vector<Customer> customers = new Vector<>();
    private int orderCount;

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public void userPage(Scanner scanner){
        String str;
        str = scanner.nextLine();
        String[] args = new String[8];
        int argc = MyString.split(str,args,3);
        if (argc==0){
            System.out.println("Command not exist");
            userPage(scanner);
        }
        switch (args[0]){
            case "chgpw":
                if (argc==3){
                    if (isPWD(args[1])){
                        if (args[1].equals(args[2])){
                            setPWD(args[1]);
                            System.out.println("Change password success");
                        }else {
                            System.out.println("Not match");
                        }
                    }else {
                        System.out.println("New password illegal");
                    }
                }else {
                    System.out.println("Params' count illegal");
                }
                break;
            case "myinfo":
                if (argc==1){
                    System.out.println(
                            "[info]\n" +
                                    "| name:\t" + getName() + "\n" +
                                    "| Sex:\t" + getSex() + "\n" +
                                    "| Pho:\t" + getPhoneNumber() + "\n" +
                                    "| PID:\t" + getPID() + "\n" +
                                    "| Pwd:\t" + getPWD() + "\n" +
                                    "| Type:\t" + getClass().getSimpleName()
                    );
                }else {
                    System.out.println("Params' count illegal");
                }
                break;
            case "back":
                if (argc==1){
                    System.out.println("Logout success");
                    return;
                }else {
                    System.out.println("Params' count illegal");
                }
                break;
            case "QUIT":
                if (argc==1){
                    System.out.println("----- Good Bye! -----");
                    System.exit(0);
                }else {
                    System.out.println("Params' count illegal");
                }
                break;
            case "gl":
                if (argc == 1){
                    int cnt =1;
                    for (Order order:OrderList.getInstance().getVector()){
                        if ((getPID().equals(order.getWaiterID()))&&!order.isDelivered()){//pid == waiter.id and order is not delivered
                            String string = "";
                            string += cnt + ". OID:"+order.getOrderID()+",DISH:[";
                            int flag = 0,now=0;
                            for (Dish dish:order.getDishList()){
                                flag++;
                            }
                            for (Dish dish:order.getDishList()){
                                string += dish.getNum()+" "+dish.getName();
                                if (now<flag-1)string += ",";
                                now++;
                            }
                            string += ']';
                            System.out.println(string);
                            cnt++;
                        }
                    }
                    if (cnt==1){
                        System.out.println("No serving order");
                    }
                }else {
                    System.out.println("Params' count illegal");
                }
                break;
            case "mo":
                if (argc == 1){
                    int cnt =1;
                    for (Order order:OrderList.getInstance().getVector()){
                        if ((getPID().equals(order.getWaiterID()))&&!order.isDelivered()){
                            cnt++;
                            order.setDelivered(true);
                            System.out.println("Manage order success");
                            break;
                        }
                    }
                    if (cnt==1){
                        System.out.println("No serving order");
                    }
                }else {
                    System.out.println("Params' count illegal");
                }
                break;
            case "sr":
                if (argc == 2){
                    Order o = null;
                    for (Order order: OrderList.getInstance().getVector()){
                        if (args[1].equals(order.getOrderID())){
                            o = order;
                            break;
                        }

                    }
                    if (o != null){
                        if (o.getWaiterID().equals(getPID())){
                            if (o.isCooked()){
                                if (!o.isCheckout()){
                                    Person p = null;
                                    for (Person person :PersonList.getInstance().getPeople()){
                                        if (o.getCustomerID().equals(person.getPID())){
                                            p =  person;
                                        }
                                    }
                                    if (p != null){
                                        if (((Customer)p).getBalance()>o.getFullPrice()||(((Customer)p).isVIP()&&((Customer)p).getBalance()>o.getFullPrice()*0.8)){
                                            String string = "";
                                            string +="OID:"+o.getOrderID()+",DISH:[";
                                            int flag = 0,now=0;
                                            for (Dish dish:o.getDishList()){
                                                flag++;
                                            }
                                            int i=0;
                                            for (Dish dish:o.getDishList()){
                                                string += dish.getName()+String.format(" %.1f",o.getNums().elementAt(i)*dish.getPrice());
                                                if (now<flag-1)string += ",";
                                                now++;
                                                i++;
                                            }
                                            string += "],TOTAL:";
                                            double price = o.getFullPrice();
                                            if (((Customer)p).isVIP()){
                                                price *= 0.8;
                                            }
                                            ((Customer)p).setBalance(((Customer)p).getBalance()-price);
                                            string += String.format("%.1f,BALANCE:%.1f",price,((Customer)p).getBalance());
                                            for (Person person: PersonList.getInstance().getPeople()){
                                                if (person.getPID().equals(o.getWaiterID())){
                                                    ((Waiter)person).setOrderCount(((Waiter)person).getOrderCount()-1);
                                                }
                                            }
                                            System.out.println(string);

                                        }else {
                                            System.out.println("Insufficient balance");
                                        }
                                    }else {
                                        System.out.println("Input illegal");
                                    }
                                }else {
                                    System.out.println("Order already checkout");
                                }
                            }else {
                                System.out.println("Order not cooked");
                            }
                        }else {
                            System.out.println("Order serve illegal");
                        }
                    }else{
                        System.out.println("Order serve illegal");
                    }
                }else {
                    System.out.println("Params' count illegal");
                }
                break;
            case "rw":
                if (argc == 3){
                    if (Menu.isDouble(args[2]) && Double.parseDouble(args[2]) < 1000.0 && Double.parseDouble(args[2]) >= 100.0){
                        boolean flag = true;
                        for (Person person:PersonList.getInstance().getPeople()){
                            if (args[1].equals(person.getPID())){
                                ((Customer)person).setBalance(Double.parseDouble(args[2])+((Customer)person).getBalance());
                                if (((Customer)person).getBalance()>=200){
                                    ((Customer) person).setVIP(true);
                                }
                                flag = false;
                                break;
                            }
                        }
                        if (flag){
                            System.out.println("Input illegal");
                        }
                    }else {
                        System.out.println("Recharge input illegal");
                    }

                }else {
                    System.out.println("Params' count illegal");
                }
                break;
            default:
                System.out.println("Command not exist");
        }
        userPage(scanner);
    }
}
