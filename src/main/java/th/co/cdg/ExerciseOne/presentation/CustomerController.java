package th.co.cdg.ExerciseOne.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import th.co.cdg.ExerciseOne.model.Customer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
public class CustomerController {

    private List<Customer> customers = new ArrayList<>();

    private boolean areCustomerExist(Long id){
        return customers
                .stream()
                .anyMatch(existedCustomer -> id.equals(existedCustomer.getId()));
    }

    // ---- Service สำหรับข้อมูล Customer ทั้งหมด ---- //
    @GetMapping(value = "get-all-customer")
    public ResponseEntity<List<Customer>> getAllCustomerController() {
        if (!customers.isEmpty()) {
            customers.sort(Comparator.comparing(Customer::getId)); //เรียงข้อมูลจาก Id จากน้อยไปมาก
            return ResponseEntity
                    .ok()
                    .body(customers);
        } else {
            return ResponseEntity
                    .noContent()
                    .build();
        }
    }

    // ---- Service สำหรับข้อมูลตาม Id ---- //

    // ---- Service สำหรับเพิ่มข้อมูล Customer ---- //
    @PostMapping(value = "add-customer")
    public ResponseEntity<String> addCustomerController(@RequestBody Customer customer) {
        if (areCustomerExist(customer.getId())) {
            return ResponseEntity
                    .badRequest()
                    .body("Customer already exist");
        } else {
            customers.add(customer);
            return ResponseEntity
                    .ok()
                    .body("Add customer successfully");
        }
    }

    // ---- Service สำหรับอัพเดตข้อมูล Customer ---- //

    // ---- Service สำหรับลบข้อมูล Customer ตาม Id ---- //

    // ---- Service สำหรับลบข้อมูล Customer ทั้งหมด ---- //

}
