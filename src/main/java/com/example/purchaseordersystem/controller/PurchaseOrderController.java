package com.example.purchaseordersystem.controller;

import com.example.purchaseordersystem.model.PurchaseOrder;
import com.example.purchaseordersystem.service.PurchaseOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PurchaseOrderController {

    private PurchaseOrderService service = new PurchaseOrderService();

    @GetMapping("/")
    public String home() {
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String viewOrders(Model model) {
        List<PurchaseOrder> orders = service.getAllOrders();
        model.addAttribute("orders", orders);
        return "orderList";
    }

    @GetMapping("/createOrder")
    public String showCreateForm() {
        return "createOrder";
    }

    @PostMapping("/createOrder")
    public String submitOrder(
            @RequestParam String supplierId,
            @RequestParam String supplierName,
            @RequestParam String expectedDate,
            @RequestParam String notes,
            @RequestParam String createdBy,
            @RequestParam("itemName[]") String[] itemNames,
            @RequestParam("quantity[]") int[] quantities,
            @RequestParam("unitPrice[]") double[] unitPrices
    ) {
        service.createOrderWithItems(
                supplierId, supplierName, expectedDate,
                notes, createdBy, itemNames, quantities, unitPrices
        );
        return "redirect:/orders";
    }

    @GetMapping("/updateOrder")
    public String showUpdateForm(@RequestParam String id, Model model) {
        PurchaseOrder po = service.getOrderById(id);
        model.addAttribute("po", po);
        return "updateStatus";
    }

    @PostMapping("/updateOrder")
    public String submitUpdate(
            @RequestParam String poId,
            @RequestParam String newStatus
    ) {
        service.updateStatus(poId, newStatus);
        return "redirect:/orders";
    }

    @GetMapping("/deleteOrder")
    public String deleteOrder(@RequestParam String id) {
        service.deleteOrder(id);
        return "redirect:/orders";
    }
}