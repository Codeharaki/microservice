package com.course.client.Controller;

import com.course.client.Beans.*;
import com.course.client.Proxies.MsCartProxy;
import com.course.client.Proxies.MsOrderProxy;
import com.course.client.Proxies.MsProductProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ClientController {
    /*@Autowired
    private MsProductProxy msProductProxy;
    @RequestMapping("/")
    public String index(Model model) {
        List<ProductBean> products = msProductProxy.list();
        model.addAttribute("products", products);
        return "index";
    }*/
    private static final DecimalFormat df = new DecimalFormat("0.00");
    @Autowired
    private MsProductProxy msProductProxy;
    @Autowired
    private MsCartProxy msCartProxy;
    @Autowired
    private MsOrderProxy msOrderProxy;

    @RequestMapping("/")
    public String addNewCart(Model model) {
        int cartSize;
        cartSize = 0;
        // Creat new cart
        model.addAttribute("cartSize", cartSize);
        ResponseEntity<CartBean> cartBeanInstance = msCartProxy.createNewCart();
        model.addAttribute("cart", cartBeanInstance.getBody());
        return "redirect:/index/cart/" + cartBeanInstance.getBody().getId();
    }
    @RequestMapping("/index/cart/{idCart}")
    public String CartContent (Model model,@PathVariable Long idCart){
        System.out.println("Cart id: "+ idCart);
        Optional<CartBean> cartBean = msCartProxy.getCart(idCart);
        int cartElements=0;
        cartElements= cartBean.get().getProducts().size();
        System.out.println( "Cart : "+ cartBean.get());
        System.out.println( "cart elements "+ cartElements);
        model.addAttribute("cart", cartBean.get());
        model.addAttribute("cartElements", cartElements);
        List<ProductBean> products =  msProductProxy.list();
        // need to get products of just the current cart
        model.addAttribute("products", products);
        return "index";
    }

    @RequestMapping("/cart/{idCart}/detailsCart")
    public String addProduct (Model model, @PathVariable Long idCart){
        Optional<CartBean> cartBean = msCartProxy.getCart(idCart);
        int cartElements=0;
        cartElements= cartBean.get().getProducts().size();
        System.out.println("Cart id: "+ idCart);
        System.out.println( "Cart : "+ cartBean.get());
        System.out.println( "cart elements "+ cartElements);

        double sum =0;
        int SumQuantity = 0;
        double quantity;
        double price;
        List<CartItemBean> cartItems= cartBean.get().getProducts();
        List<Optional<ProductBean>> products = new ArrayList<Optional<ProductBean>>();
        for (CartItemBean cartItem: cartItems ){
            Optional<ProductBean> product = msProductProxy.get(cartItem.getProductId());
            products.add(product);
            quantity = cartItem.getQuantity();
            price = product.get().getPrice();
            sum += quantity * price;
            SumQuantity += quantity;
        }

        System.out.println("Cart Total:" + df.format(sum));
        model.addAttribute("cartElements", cartElements);
        model.addAttribute("Sum", df.format(sum));
        model.addAttribute("SumQuantity", SumQuantity);
        model.addAttribute("cart", cartBean.get());

        //List<ProductBean> products =  msProductProxy.get();
        model.addAttribute("cartItems", products);
        return "cart";
    }

    @RequestMapping("/cart/{idCart}/product-detail/{id}")
    public String  productDetails(Model model,@PathVariable Long id, @PathVariable Long idCart){

        Optional<CartBean> cartBean = msCartProxy.getCart(idCart);
        int cartElements=0;
        cartElements= cartBean.get().getProducts().size();

        System.out.println("Cart id: "+ idCart);
        System.out.println( "Cart : "+ cartBean.get());
        System.out.println( "Cart elements "+ cartElements);

        Optional<ProductBean> productBean = msProductProxy.get(id);
        model.addAttribute("product",productBean.get() );
        model.addAttribute("cartElements", cartElements);
        model.addAttribute("cart", cartBean.get());
        return "product";
    }

    @RequestMapping("/cart/{idCart}/product-add/{idProduct}")
    public String addProductToCart (Model model, @PathVariable Long idCart, @PathVariable Long idProduct){
        System.out.println("Cart id: "+ idCart);
        Optional<CartBean> cart =  msCartProxy.getCart(idCart);
        if (cart.isPresent() == false) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cart not found");
        }
        CartItemBean cartItem= new CartItemBean();
        cartItem.setProductId(idProduct);
        cartItem.setQuantity(1);
        msCartProxy.addProductToCart(idCart,cartItem);
        List<ProductBean> products =  msProductProxy.list();
        model.addAttribute("products", products);

        int cartElements=cart.get().getProducts().size();
        model.addAttribute("cartElements", cartElements);
        System.out.println("Cart elements : " + cartElements);
        model.addAttribute("cart", cart.get());

        return "redirect:/index/cart/" + cart.get().getId();
    }

    @RequestMapping("/cart/{idCart}/Order")
    public String placeOrder(Model model, @PathVariable Long idCart){
        ResponseEntity<OrderBean> orderBeanInstance = msOrderProxy.createNewOrder();
        System.out.println("Order created :" + orderBeanInstance.getBody().getId());
        Optional<CartBean> cart =  msCartProxy.getCart(idCart);
        System.out.println("Cart elements: "+ cart.get().getProducts());
        // Put Cart items in order
        Optional<ProductBean> productInstance = null;
        OrderItemBean ItemToAdd = null;
        for (CartItemBean cartItem: cart.get().getProducts()){
            productInstance = msProductProxy.get(cartItem.getProductId());
            ItemToAdd = new OrderItemBean( productInstance.get().getId(), cartItem.getQuantity(),
                    productInstance.get().getIllustration(), productInstance.get().getDescription(), productInstance.get().getPrice());
            msOrderProxy.addOrderItemToOrder(orderBeanInstance.getBody().getId(), ItemToAdd);
        }
        double sum =0;
        int SumQuantity = 0;
        double quantity;
        double price;
        for (CartItemBean cartItem: cart.get().getProducts()){
            Optional<ProductBean> product = msProductProxy.get(cartItem.getProductId());
            quantity = cartItem.getQuantity();
            price = product.get().getPrice();
            sum += quantity * price;
            SumQuantity += quantity;
        }

        System.out.println("Cart Total:" + df.format(sum));
        int cartElements=cart.get().getProducts().size();
        model.addAttribute("cartElements", cartElements);
        model.addAttribute("Sum", df.format(sum));
        model.addAttribute("SumQuantity", SumQuantity);

        // Suppression Cart
        cart.get().deleteCart();
        System.out.println("Order" + orderBeanInstance.getBody().toString() );
        System.out.println("Order Placed");
        return "order";
    }

}










