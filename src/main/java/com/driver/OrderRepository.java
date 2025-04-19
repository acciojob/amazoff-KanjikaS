package com.driver;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository() {
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order) {
        // your code here
        orderMap.put(order.getId(), order);
    }

    public void savePartner(String partnerId) {
        // your code here
        // create a new partner with given partnerId and save it
        partnerMap.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void saveOrderPartnerMap(String orderId, String partnerId) {
        if (orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)) {
            // your code here
            //add order to given partner's order list
            System.out.println("Before adding order " + orderId + " to partner " + partnerId + ":");
            partnerToOrderMap.putIfAbsent(partnerId, new HashSet<>());
            //increase order count of partner
            partnerToOrderMap.get(partnerId).add(orderId);

            //assign partner to this order
            orderToPartnerMap.put(orderId, partnerId);
            partnerMap.get(partnerId).setNumberOfOrders(partnerToOrderMap.get(partnerId).size());
            System.out.println("After adding order " + orderId + " to partner " + partnerId + ":");
        }
    }

    public Order findOrderById(String orderId) {
        // your code here
        return orderMap.get(orderId);
    }

    public DeliveryPartner findPartnerById(String partnerId) {
        // your code here
        partnerId = partnerId.trim();
        DeliveryPartner partner = partnerMap.get(partnerId);
        if (partner != null) {
            System.out.println("Found partner: " + partner);
        } else {
            System.out.println("Partner not found: '" + partnerId + "'");
        }
        return partner;
    }

    public Integer findOrderCountByPartnerId(String partnerId) {
        // your code here
        return partnerToOrderMap.get(partnerId).size();
    }

    public List<String> findOrdersByPartnerId(String partnerId) {
        // your code here
        return new ArrayList<>(partnerToOrderMap.get(partnerId));
    }

    public List<String> findAllOrders() {
        // your code here
        // return list of all orders
        return new ArrayList<>(orderMap.keySet());
    }

    public void deletePartner(String partnerId) {
        // your code here
        // delete partner by ID
        if (partnerMap.containsKey(partnerId)) {
            if (partnerToOrderMap.containsKey(partnerId)) {
                for (String orderId : partnerToOrderMap.get(partnerId)) {
                    orderToPartnerMap.remove(orderId);
                }
                partnerToOrderMap.remove(partnerId);
            }
            partnerMap.remove(partnerId);
        }
    }

    public void deleteOrder(String orderId) {
        // your code here
        // delete order by ID
        if (orderMap.containsKey(orderId)) {
            if (orderToPartnerMap.containsKey(orderId)) {
                String partnerId = orderToPartnerMap.get(orderId);
                partnerToOrderMap.get(partnerId).remove(orderId);
                partnerMap.get(partnerId).setNumberOfOrders(partnerToOrderMap.get(partnerId).size());
                orderToPartnerMap.remove(orderId);
            }
            orderMap.remove(orderId);
        }
    }

    public Integer findCountOfUnassignedOrders() {
        // your code here
        return orderMap.size() - orderToPartnerMap.size();
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId) {

        if (!partnerToOrderMap.containsKey(partnerId)) {
            return 0;
        }
        String[] time = timeString.split(":");
        int givenMinutes = Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1]);


        int count = 0;
        for (String orderId : partnerToOrderMap.get(partnerId)) {
            Order order = orderMap.get(orderId);
            if (order != null && order.getDeliveryTime() > givenMinutes) {
                count++;
            }
        }

        System.out.println("Orders left after " + timeString + " for partner " + partnerId + ": " + count);
        return count;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId) {
        if (!partnerToOrderMap.containsKey(partnerId)) return "00:00";

        int latestTime = 0;

        for (String orderId : partnerToOrderMap.get(partnerId)) {
            Order order = orderMap.get(orderId);
            if (order != null) {
                latestTime = Math.max(latestTime, order.getDeliveryTime()); // No need to convert
            }
        }

        return String.format("%02d:%02d", latestTime / 60, latestTime % 60);
    }
}