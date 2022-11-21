package com.cooee.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooee.responsemessgae.CONSTANTMESSAGE;
import com.cooee.service.OrderPaymentService;


@CrossOrigin
@RestController
@RequestMapping("/StripeController")
public class StripeController {
	
	@Autowired
	private OrderPaymentService orderPaymentService;
//	@ApiOperation(value = "Form-data api_version will generate based on SDK")
    @PostMapping("/get_ephemeral_key")
    public Map<Object, Object> getEphemeralKey(
            @RequestParam("api_version") String api_version,
            @RequestParam("Id") Long Id) {
        HashMap<Object, Object> map = new HashMap<>();
        
            return orderPaymentService.getEphemeralKey(api_version, Id);
    
       
            }
    
    @PostMapping("/client_secret")
    public Map<Object, Object> clientSecret( @RequestParam("currencyType") String currencyType,@RequestParam("amount") Double amount, @RequestParam("Id") Long userId) {
        HashMap<Object, Object> map = new HashMap<>();
        
            return orderPaymentService.clientSecret(currencyType, amount, userId);
    
       
            }
    
    //
	@PostMapping("/intentId")
	public Map<Object, Object> intentId(@RequestParam("id") String intentId) {

		return orderPaymentService.getIntentId(intentId);

	}

	//
	@PostMapping("/subscription")
	public Map<Object, Object> subscription(@RequestParam("price") String price,
			@RequestParam("customer") String customer, @RequestParam("paymentMethod") String paymentMethod) {

		return orderPaymentService.subscription(price, customer, paymentMethod);

	}
    
//    @PostMapping("/createProduct")
//    public Map<Object, Object> createProduct(@RequestParam("productName") String productName) {
//        HashMap<Object, Object> map = new HashMap<>();
//
//        return orderPaymentService.createProduct(productName);
//
//    }
//
//    @PostMapping("/createPlan")
//    public Map<Object, Object> createPlan(@RequestParam("amount") Integer amount,
//            @RequestParam("currency") String currency, @RequestParam("interval") String interval,
//            @RequestParam("product") String product) {
//        HashMap<Object, Object> map = new HashMap<>();
//
//        return orderPaymentService.createPlan(amount, currency, interval, product);
//
//    }

//    @PostMapping("/subscription")
//    public Map<Object, Object> subscription(@RequestParam("price") String price,
//            @RequestParam("customer") String customer) {
//        HashMap<Object, Object> map = new HashMap<>();
//
//        return orderPaymentService.subscription(price, customer);
//
//    }


}
