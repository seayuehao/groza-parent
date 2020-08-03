package open.iot.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class CustomerController extends BaseController {

    public static final String CUSTOMER_ID = "customerId";
    public static final String IS_PUBLIC = "isPublic";

}
