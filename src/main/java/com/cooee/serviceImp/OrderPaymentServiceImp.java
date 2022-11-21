package com.cooee.serviceImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cooee.model.User;
import com.cooee.model.UserSubscriptionPlan;
import com.cooee.payload.ClientSecreatPayload;
import com.cooee.repository.UserRepository;
import com.cooee.responsemessgae.CONSTANTMESSAGE;
import com.cooee.service.OrderPaymentService;
import com.cooee.util.Constant;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.EphemeralKey;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Plan;
import com.stripe.model.Product;
import com.stripe.model.Subscription;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class OrderPaymentServiceImp implements OrderPaymentService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public Map<Object, Object> getEphemeralKey(String api_version, Long Id) {
		Map<Object, Object> response = new HashMap<Object, Object>();
		Stripe.apiKey = CONSTANTMESSAGE.Secretkey;
		try {
			// api_version=2020-08-27
			User uDetail = userRepository.findById(Id).get();

//                get_ephemeral_key
			RequestOptions requestOptions = (new RequestOptions.RequestOptionsBuilder())
					.setStripeVersionOverride(api_version).build();

			Map<String, Object> options = new HashMap<String, Object>();
			options.put("customer", uDetail.getStipeCustomerId());

			EphemeralKey key = EphemeralKey.create(options, requestOptions);
			String payload = key.toJson().toString();

			// System.out.println("EphemeralKey=" + key.getId());
			if (payload != null) {
				// response.put(IConstant.EPHEMERALKEY, key.getId());
				// System.out.println(gson.toJson(key));
				// response.put(IConstant.RESPONSE, gson.toJson(key));

				response.put(CONSTANTMESSAGE.RESPONSE, CONSTANTMESSAGE.SUCCESSFULLY);
				response.put(CONSTANTMESSAGE.MESSAGE, CONSTANTMESSAGE.RECORDS_FOUND_SUCCESSFULLY);
				response.put(CONSTANTMESSAGE.KEY_RESPONSE, key.toJson().replaceAll("\n", ""));

				// response.put(IConstant.KEY_RESPONSE, jsonFormattedString);
			}

			else {
				response.put(CONSTANTMESSAGE.RESPONSE, CONSTANTMESSAGE.NOT_AUTHORIZED);
				response.put(CONSTANTMESSAGE.MESSAGE, CONSTANTMESSAGE.DATA_NOT_FOUND);
			}
		} catch (StripeException e) {
			e.printStackTrace();
			response.put(CONSTANTMESSAGE.RESPONSE, CONSTANTMESSAGE.NOT_AUTHORIZED);
			response.put(CONSTANTMESSAGE.MESSAGE, CONSTANTMESSAGE.DATA_NOT_FOUND);
		}

		return response;

	}

	@Override
	public Map<Object, Object> clientSecret(String currencyType, Double amount, Long userId) {
		Map<Object, Object> response = new HashMap<Object, Object>();
		try {
			ClientSecreatPayload newResponse = new ClientSecreatPayload();
			String clientSecret = null;
			String IntentId = null;
			// Stripe.apiKey = CONSTANTMESSAGE.STRIPE_API_KEY;
			Stripe.apiKey = CONSTANTMESSAGE.Secretkey;
			Long longValue = amount.longValue();
			User userDetail = userRepository.findById(userId).get();
			PaymentIntentCreateParams params = PaymentIntentCreateParams.builder().setAmount(longValue)
					.setCustomer(userDetail.getStipeCustomerId()).setCurrency(currencyType).build();
			PaymentIntent intent = PaymentIntent.create(params);
			clientSecret = intent.getClientSecret();
			IntentId = intent.getId();
			newResponse.setClientSecret(clientSecret);
			newResponse.setIntentId(IntentId);
			if (clientSecret != null || IntentId != null) {
				response.put(CONSTANTMESSAGE.RESPONSE, CONSTANTMESSAGE.SUCCESSFULLY);
				response.put(CONSTANTMESSAGE.MESSAGE, CONSTANTMESSAGE.RECORDS_FOUND_SUCCESSFULLY);
				response.put(CONSTANTMESSAGE.OBJECT_RESPONSE, newResponse);
				// response.put(CONSTANTMESSAGE.OBJECT_RESPONSE,IntentId );
				return response;
			}
			System.out.println("done");

		} catch (Exception e) {
			e.printStackTrace();
			response.put(CONSTANTMESSAGE.RESPONSE, CONSTANTMESSAGE.NOT_AUTHORIZED);
			response.put(CONSTANTMESSAGE.MESSAGE, CONSTANTMESSAGE.CLIENT_SECRETE_FAILED);
		}

//            } catch (Exception e) {
//    
//    }
		return response;
	}
	@Override
    public Map<Object, Object> getIntentId(String intentId) {
        Map<Object, Object> response = new HashMap<Object, Object>();
        Stripe.apiKey = CONSTANTMESSAGE.Secretkey;

       // To create a PaymentIntent for confirmation, see our guide at:
        // https://stripe.com/docs/payments/payment-intents/creating-payment-intents#creating-for-automatic
        PaymentIntent paymentIntent;
        try {
            paymentIntent = PaymentIntent.retrieve(intentId);

//            Map<String, Object> params = new HashMap<>();
//            params.put("payment_method", "pm_card_visa");
//
//            PaymentIntent updatedPaymentIntent =
//              paymentIntent.confirm(params);
            response.put(CONSTANTMESSAGE.RESPONSE, CONSTANTMESSAGE.SUCCESSFULLY);
            response.put(CONSTANTMESSAGE.MESSAGE, CONSTANTMESSAGE.RECORDS_FOUND_SUCCESSFULLY);
            response.put(CONSTANTMESSAGE.DATA, paymentIntent.toJson().toString());
            return response;

       } catch (StripeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        response.put(CONSTANTMESSAGE.RESPONSE, CONSTANTMESSAGE.SUCCESSFULLY);
        response.put(CONSTANTMESSAGE.MESSAGE, CONSTANTMESSAGE.RECORDS_FOUND_SUCCESSFULLY);
//        response.put(CONSTANTMESSAGE.DATA, paymentIntent);
        return response;
    }



	@Override
	public Map<Object, Object> subscription(String price, String customer, String paymentMethod) {
		 Map<Object, Object> response = new HashMap<Object, Object>();
       Stripe.apiKey = CONSTANTMESSAGE.Secretkey;
      List<Object> items = new ArrayList<>();
       Map<String, Object> item1 = new HashMap<>();
       item1.put("price",price);
       items.add(item1);
       Map<String, Object> params = new HashMap<>();
       params.put("customer", customer);
       params.put("items", items);
       params.put("payment_Method", paymentMethod);
       params.put("default_payment_method", paymentMethod);
      try {
          Subscription subscription = Subscription.create(params);
          response.put(CONSTANTMESSAGE.RESPONSE, CONSTANTMESSAGE.SUCCESSFULLY);
          response.put(CONSTANTMESSAGE.MESSAGE, CONSTANTMESSAGE.RECORDS_FOUND_SUCCESSFULLY);
          response.put(CONSTANTMESSAGE.DATA, subscription.getId());
          return response;
      } catch (Exception e) {
          
          e.printStackTrace();
          response.put(CONSTANTMESSAGE.RESPONSE, CONSTANTMESSAGE.NOT_AUTHORIZED);
          response.put(CONSTANTMESSAGE.MESSAGE, CONSTANTMESSAGE.DATA_NOT_FOUND);
          return response;
      }
		


//
//	@Override
//	public Map<Object, Object> subscription(String price, String customer, String paymentMethod) {
//		Map<Object, Object> response = new HashMap<Object, Object>();
//		Stripe.apiKey = CONSTANTMESSAGE.Secretkey;
//
//		List<Object> items = new ArrayList<>();
//		Map<String, Object> item1 = new HashMap<>();
//		item1.put("price", price);
//		items.add(item1);
//		Map<String, Object> params = new HashMap<>();
//		params.put("customer", customer);
//		params.put("items", items);
//		params.put("payment_behavior", "default_incomplete");
//
//		try {
//			Subscription subscription = Subscription.create(params);
//			
//			UserSubscriptionPlan userSubscriptionPlan = new UserSubscriptionPlan();
//			userSubscriptionPlan.setPaymentMethod(paymentMethod);
//			userSubscriptionPlan.setSubscriptionStripeId(subscription.getId());
//			
//			response.put(CONSTANTMESSAGE.RESPONSE, CONSTANTMESSAGE.SUCCESS);
//			response.put(CONSTANTMESSAGE.DATA, subscription.getId());
//			return response;
//		} catch (Exception e) {
//			e.printStackTrace();
//			response.put(CONSTANTMESSAGE.RESPONSE, CONSTANTMESSAGE.NOT_AUTHORIZED);
//			response.put(CONSTANTMESSAGE.MESSAGE, CONSTANTMESSAGE.DATA_NOT_FOUND);
//			return response;
//		}
//	}
       
//       
//       @Override
//       public Map<Object, Object> webhook(String intentId) {
//           Map<Object, Object> response = new HashMap<Object, Object>();
//           Stripe.apiKey = CONSTANTMESSAGE.Secretkey;
//          // To create a PaymentIntent for confirmation, see our guide at: https://stripe.com/docs/payments/payment-intents/creating-payment-intents#creating-for-automatic
//           PaymentIntent paymentIntent;
//           try {
//               paymentIntent = PaymentIntent.retrieve(intentId);
//               response.put(CONSTANTMESSAGE.RESPONSE, CONSTANTMESSAGE.SUCCESSFULLY);
//               response.put(CONSTANTMESSAGE.MESSAGE, CONSTANTMESSAGE.RECORDS_FOUND_SUCCESSFULLY);
//               response.put(CONSTANTMESSAGE.DATA, paymentIntent.toJson().toString());
//               return response;
//               
//           } catch (StripeException e) {
//               // TODO Auto-generated catch block
//               e.printStackTrace();
//           }
//           response.put(CONSTANTMESSAGE.RESPONSE, CONSTANTMESSAGE.SUCCESSFULLY);
//           response.put(CONSTANTMESSAGE.MESSAGE, CONSTANTMESSAGE.RECORDS_FOUND_SUCCESSFULLY);
////           response.put(CONSTANTMESSAGE.DATA, paymentIntent);
//           return response;
//   }


//	@Override
//    public Map<Object, Object> createProduct(String productName) {
//        Map<Object, Object> response = new HashMap<Object, Object>();
//        Stripe.apiKey = CONSTANTMESSAGE.Secretkey;
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("name", "COOEE PRODUCT");
//
//        try {
//            Product product = Product.create(params);
//            response.put(CONSTANTMESSAGE.RESPONSE, CONSTANTMESSAGE.SUCCESSFULLY);
//            response.put(CONSTANTMESSAGE.MESSAGE, CONSTANTMESSAGE.RECORDS_FOUND_SUCCESSFULLY);
//            response.put(CONSTANTMESSAGE.DATA, product.getId());
//            return response;
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            response.put(CONSTANTMESSAGE.RESPONSE, CONSTANTMESSAGE.NOT_AUTHORIZED);
//            response.put(CONSTANTMESSAGE.MESSAGE, CONSTANTMESSAGE.DATA_NOT_FOUND);
//            return response;
//        }
//
//    }
//
//    @Override
//    public Map<Object, Object> createPlan(Integer amount, String currency, String interval, String product) {
//        Map<Object, Object> response = new HashMap<Object, Object>();
//        Stripe.apiKey = CONSTANTMESSAGE.Secretkey;
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("amount", amount);
//        params.put("currency", currency);
//        params.put("interval", interval);
//        params.put("product", product);
//
//        try {
//            Plan plan = Plan.create(params);
//            response.put(CONSTANTMESSAGE.RESPONSE, CONSTANTMESSAGE.SUCCESSFULLY);
//            response.put(CONSTANTMESSAGE.MESSAGE, CONSTANTMESSAGE.RECORDS_FOUND_SUCCESSFULLY);
//            response.put(CONSTANTMESSAGE.DATA, plan.getId());
//            return response;
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//
//            e.printStackTrace();
//            response.put(CONSTANTMESSAGE.RESPONSE, CONSTANTMESSAGE.NOT_AUTHORIZED);
//            response.put(CONSTANTMESSAGE.MESSAGE, CONSTANTMESSAGE.DATA_NOT_FOUND);
//            return response;
//        }
//
//    }

//    @Override
//    public Map<Object, Object> subscription(String price, String customer) {
//        Map<Object, Object> response = new HashMap<Object, Object>();
//        Stripe.apiKey = CONSTANTMESSAGE.Secretkey;
//
//        List<Object> items = new ArrayList<>();
//        Map<String, Object> item1 = new HashMap<>();
//        item1.put("price",price );
//        items.add(item1);
//        Map<String, Object> params = new HashMap<>();
//        params.put("customer", customer);
//        params.put("items", items);
//        params.put("payment_behavior", "default_incomplete");
//
//       try {
//            Subscription subscription = Subscription.create(params);
//
//            response.put(CONSTANTMESSAGE.RESPONSE, CONSTANTMESSAGE.SUCCESSFULLY);
//            response.put(CONSTANTMESSAGE.MESSAGE, CONSTANTMESSAGE.RECORDS_FOUND_SUCCESSFULLY);
//            response.put(CONSTANTMESSAGE.DATA, subscription.getId());
//            return response;
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            response.put(CONSTANTMESSAGE.RESPONSE, CONSTANTMESSAGE.NOT_AUTHORIZED);
//            response.put(CONSTANTMESSAGE.MESSAGE, CONSTANTMESSAGE.DATA_NOT_FOUND);
//            return response;
//        }
//    }

//}
	}
	}
