package com.driver;

import java.util.*;
import static java.lang.Math.max;

import org.springframework.stereotype.Repository;
import static java.lang.Math.max;
import org.springframework.beans.factory.annotation.Autowired;
@Repository
public class OrderRepository {

    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository(){
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order){
        // your code here
        orderMap.put(order.getId(),order);
    }

    public void savePartner(String partnerId){
        // your code here
        // create a new partner with given partnerId and save it
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        partnerMap.put(partner.getId(),partner);
    }

    public void saveOrderPartnerMap(String orderId, String partnerId){
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
            // your code here
            //add order to given partner's order list
            //increase order count of partner
            //assign partner to this order
            partnerToOrderMap.putIfAbsent(partnerId,new HashSet<>());
            partnerToOrderMap.get(partnerId).add(orderId);
            DeliveryPartner partner = partnerMap.get(partnerId);
            partner.setNumberOfOrders(partnerToOrderMap.get(partnerId).size());
            orderToPartnerMap.put(orderId,partnerId);
        }
    }

    public Order findOrderById(String orderId){
        // your code here
        if(orderMap.containsKey(orderId)){
            return orderMap.get(orderId);
        }
        return null;
    }

    public DeliveryPartner findPartnerById(String partnerId){
        // your code here
        if(partnerMap.containsKey(partnerId)){
            return partnerMap.get(partnerId);
        }
        return null;
    }

    public Integer findOrderCountByPartnerId(String partnerId){
        // your code here
        if(partnerMap.containsKey(partnerId)){
            return partnerMap.get(partnerId).getNumberOfOrders();
        }
        return Integer.valueOf(0);
    }

    public List<String> findOrdersByPartnerId(String partnerId){
        // your code here
        List<String> orderOfPartner = new ArrayList<>();
        if(partnerToOrderMap.containsKey(partnerId)){
            orderOfPartner.addAll(partnerToOrderMap.get(partnerId));
        }
        return orderOfPartner;
    }

    public List<String> findAllOrders(){
        // your code here
       return new ArrayList<>(orderMap.keySet());
    }

    public void deletePartner(String partnerId){
        // your code here
        // delete partner by ID
        partnerMap.remove(partnerId);
        HashSet<String> orderIdsToUnassign = partnerToOrderMap.remove(partnerId);

        if(orderIdsToUnassign != null && !orderIdsToUnassign.isEmpty()){
            for(String orderId : orderIdsToUnassign){
                orderToPartnerMap.remove(orderId);
            }
        }
    }

    public void deleteOrder(String orderId){
        // your code here
        // delete order by ID
        orderMap.remove(orderId);
        String partnerId = orderToPartnerMap.remove(orderId);

        if (partnerId != null && partnerToOrderMap.containsKey(partnerId)) {
            partnerToOrderMap.get(partnerId).remove(orderId);
            if (partnerToOrderMap.get(partnerId).isEmpty()) {
                partnerToOrderMap.remove(partnerId);
            }
        }
    }

    public Integer findCountOfUnassignedOrders(){
        // your code here
        int countOfUnassignedOrders = 0;

        for(String orderId : orderMap.keySet()){
            if(!orderToPartnerMap.containsKey(orderId)){
                countOfUnassignedOrders++;
            }
        }

        return Integer.valueOf(countOfUnassignedOrders);
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId) {
        int count = 0;
        try {
            int hh = Integer.parseInt(timeString.substring(0, 2));
            int mm = Integer.parseInt(timeString.substring(3));
            int givenTime = hh * 60 + mm;

            HashSet<String> orderIdList = partnerToOrderMap.get(partnerId);
            if (orderIdList == null || orderIdList.isEmpty()) {
                return 0;  // If no orders for the partner
            }

            for (String orderId : orderIdList) {
                Order order = orderMap.get(orderId);
                if (order != null && givenTime > order.getDeliveryTime()) {
                    count++;
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid time format or unexpected error", e);
        }

        return count;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId){
        // your code here
        // code should return string in format HH:MM
        if(!partnerMap.containsKey(partnerId)){
            return "No Partner exist with this partnerId";
        }

        if (!partnerToOrderMap.containsKey(partnerId) || partnerToOrderMap.get(partnerId).isEmpty()) {
            return "No orders assigned";
        }
        int lastDeliveryTime = 0;
        HashSet<String> orderIdList = partnerToOrderMap.get(partnerId);

        for(String x : orderIdList){
            int deliveryTime = orderMap.get(x).getDeliveryTime();
            lastDeliveryTime = max(lastDeliveryTime,deliveryTime);
        }
        return String.format("%02d:%02d",lastDeliveryTime/60,lastDeliveryTime%60);
    }
}