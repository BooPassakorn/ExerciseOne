package th.co.cdg.ExerciseOne.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    private void deleteCustomer(Customer customer){
        customers.removeIf(existedCustomer -> customer.getId().equals(existedCustomer.getId()));
    }

    private void addCustomer (Customer customer){
        customers.add(customer);
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
    @GetMapping(value = "get-customer/{id}")
    public ResponseEntity<Customer> getCustomerByIdController(@PathVariable(name = "id") long id){
        Customer customer = customers
                .stream()
                .filter(existedCustomer -> existedCustomer.getId().equals(id))
                .findAny()
                .orElse(null);

        if (customer != null) {
            return ResponseEntity
                    .ok()
                    .body(customer);
        } else {
            return ResponseEntity
                    .noContent()
                    .build();
        }
    }

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
    @PutMapping(value = "update-customer")
    public ResponseEntity<String> updateCustomerController(@RequestBody Customer customer) {

        if (null == customer.getId()) {
            return ResponseEntity
                    .badRequest()
                    .body("Cannot update customer: field 'id' is missing.");
        }

        Customer queryCustomer = customers
                .stream()
                .filter(existedCustomer -> customer.getId().equals(existedCustomer.getId()))
                .findAny()
                .orElse(null);
        if (null == queryCustomer) {
            return ResponseEntity
                    .badRequest()
                    .body("Cannot update customer: Customer does not exist.");
        }

        if (null != customer.getName()) queryCustomer.setName(customer.getName());
        if (null != customer.getSurname()) queryCustomer.setSurname(customer.getSurname());
        if (null != customer.getAddress()) queryCustomer.setAddress(customer.getAddress());
        if (null != customer.getAge()) queryCustomer.setAge(customer.getAge());
        if (null != customer.getTel()) queryCustomer.setTel(customer.getTel());

        deleteCustomer(customer);
        addCustomer(queryCustomer);

        return ResponseEntity
                .ok()
                .body("Customer successfully updated");
    }

    // ---- Service สำหรับลบข้อมูล Customer ตาม Id ---- //
    @DeleteMapping(value = "delete-customer/{id}")
    public ResponseEntity<String> deleteCustomerByIdController(@PathVariable(name = "id") long id) {
        if (areCustomerExist(id)) {
            customers.removeIf(customer -> customer.getId().equals(id));
            return ResponseEntity
                    .ok()
                    .body("Customer successfully deleted");
        } else {
            return ResponseEntity
                    .badRequest()
                    .body("Customer does not exist");
        }
    }

    // ---- Service สำหรับลบข้อมูล Customer ทั้งหมด ---- //
    @DeleteMapping(value = "delete-all-customer")
    public ResponseEntity<String> deleteAllCustomerController() {
        if (!customers.isEmpty()) {
            customers.clear();
            return ResponseEntity
                    .ok()
                    .body("All customer successfully deleted");
        } else {
            return ResponseEntity
                    .badRequest()
                    .body("No customer found");
        }
    }
}
