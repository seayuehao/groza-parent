package open.iot.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author james mu
 * @date 19-1-25 下午2:28
 */
@RestController
@RequestMapping("/api")
public class CustomerController extends BaseController {

    public static final String CUSTOMER_ID = "customerId";
    public static final String IS_PUBLIC = "isPublic";

}
