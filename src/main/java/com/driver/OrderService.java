package com.driver;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository = new OrderRepository();

    public void addOrder(Order order){
        orderRepository.saveOrder(order);
    }

    public void addPartner(String partnerId){
        System.out.println("Adding Partner");
        orderRepository.savePartner(partnerId);

    }

    public void createOrderPartnerPair(String orderId, String partnerId){
        System.out.println("creating partner order pair");
        orderRepository.saveOrderPartnerMap(orderId, partnerId);
    }

    public Order getOrderById(String orderId){
        System.out.println("Fetching order by ID " +orderId);
        return orderRepository.findOrderById(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId){
        System.out.println("Get Partners by ID " + partnerId);
        return orderRepository.findPartnerById(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId){
        System.out.println("Get Order Count by Partner ID");
        return orderRepository.findOrderCountByPartnerId(partnerId);
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        return orderRepository.findOrdersByPartnerId(partnerId);
    }

    public List<String> getAllOrders(){
        return orderRepository.findAllOrders();
    }

    public void deletePartner(String partnerId){
        orderRepository.deletePartner(partnerId);
    }

    public void deleteOrder(String orderId){
        orderRepository.deleteOrder(orderId);
    }

    public Integer getCountOfUnassignedOrders(){
        return orderRepository.findCountOfUnassignedOrders();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){

        return orderRepository.findOrdersLeftAfterGivenTimeByPartnerId(time, partnerId);
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId){
        return orderRepository.findLastDeliveryTimeByPartnerId(partnerId);
    }
}