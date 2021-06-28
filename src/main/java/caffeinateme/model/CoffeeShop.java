package caffeinateme.model;

import java.util.*;

public class CoffeeShop {

    private Queue<Order> orders = new LinkedList<>();

    public void placeOrder(Order order, int distanceInMetres) {
        if (distanceInMetres <= 50) {
            order = order.withStatus(OrderStatus.Urgent);
        }
        else {
            order = order.withStatus(OrderStatus.Normal);
        }
        orders.add(order);

    }

    public List<Order> getPendingOrders() {
        return new ArrayList<>(orders);
    }

    public Optional<Order> getOrderFor(Customer customer) {
        return orders.stream()
                .filter( order -> order.getCustomer().equals(customer))
                .findFirst();
    }
}
