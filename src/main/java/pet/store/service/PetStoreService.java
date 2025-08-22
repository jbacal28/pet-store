package pet.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class PetStoreService {

  @Autowired
  private PetStoreDao petStoreDao;
  
  public PetStoreData savePetStore(PetStoreData petStoreData) {
    
    PetStore petStore = findOrCreatePetStore(petStoreData.getPetStoreId());
    
    copyPetStoreFields(petStore, petStoreData);
    
    PetStore dbPetStore = petStoreDao.save(petStore);
    
    return new PetStoreData(dbPetStore);
  }

  private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
    petStore.setPetStoreId(petStoreData.getPetStoreId());
    petStore.setPetStoreName(petStoreData.getPetStoreName());
    petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
    petStore.setPetStoreCity(petStoreData.getPetStoreCity());
    petStore.setPetStoreState(petStoreData.getPetStoreState());
    petStore.setPetStoreZip(petStoreData.getPetStoreZip());
    petStore.setPetStorePhone(petStoreData.getPetStorePhone());
    
  }
  
  private PetStore findOrCreatePetStore(Long petStoreId) {
          PetStore petStore;
          
          if(Objects.isNull(petStoreId)) {
            petStore = new PetStore();
          }
          else {
            petStore = findPetStoreById(petStoreId);
            
          }
    return petStore;
  }
 
  private PetStore findPetStoreById(Long petStoreId) {
    return petStoreDao.findById(petStoreId).orElseThrow(() 
        -> new NoSuchElementException("Pet store with ID=" + petStoreId + "does not exist."));
  }
  
  //week 15
  
  @Autowired
  private EmployeeDao employeeDao;
  
  private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
    employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
    employee.setEmployeeId(petStoreEmployee.getEmployeeId());
    employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
    employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
    employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
 }
  
  private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
    customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
    customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
    customer.setCustomerId(petStoreCustomer.getCustomerId());
    customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
  }

private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
    if(Objects.isNull(employeeId)) {
      return new Employee();
    }
      return findEmployeeById(petStoreId, employeeId);
    }
  
private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
  if(Objects.isNull(customerId)) {
    return new Customer();
  }
    return findCustomerById(petStoreId, customerId);
    
  }


  private Employee findEmployeeById(Long petStoreId, Long employeeId) {
    Employee employee = employeeDao.findById(employeeId)
        .orElseThrow(() -> new NoSuchElementException("Employee with ID =" + employeeId + 
            "employee not found"));
    
    if(employee.getPetStore().getPetStoreId() != petStoreId) {
      throw new IllegalArgumentException("The employee with ID =" + employeeId + 
          "employee does not belong to pet store" 
          + petStoreId + ".");
      
    }
    return employee;
  }

  private Customer findCustomerById(Long petStoreId, Long customerId) { 
    Customer customer = customerDao.findById(customerId)
        .orElseThrow(() -> new NoSuchElementException("customer with ID=" +customerId + 
            " was not found."));
    
    boolean found = false;
       
    for(PetStore petStore : customer.getPetStores()) {
      if(petStore.getPetStoreId() == petStoreId) {
        found = true;
        break;
      }
    }
    
    if(!found) {
      throw new IllegalArgumentException("The customer with ID=" + customerId + 
          " is not a member of the pet store with ID=" + petStoreId);
    }
    return customer;
  }
  
  @Transactional(readOnly = false)
  public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
    
    PetStore petStore = findPetStoreById(petStoreId);
    Long employeeId = petStoreEmployee.getEmployeeId();
    Employee employee = findOrCreateEmployee(petStoreId, employeeId);
    
    copyEmployeeFields(employee, petStoreEmployee);
    
    employee.setPetStore(petStore);
    petStore.getEmployees().add(employee);
    
    Employee dbEmployee = employeeDao.save(employee);
    
    return new PetStoreEmployee(dbEmployee);

  }
  
  @Autowired CustomerDao customerDao;
  
  @Transactional
    public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
      

      PetStore petStore = findPetStoreById(petStoreId);
      Long customerId = petStoreCustomer.getCustomerId();
      System.out.println(customerId);
      Customer customer = findOrCreateCustomer(petStoreId, customerId);
      
      copyCustomerFields(customer, petStoreCustomer);
      
  customer.getPetStores().add(petStore);
  petStore.getCustomers().add(customer);
  
  Customer dbCustomer = customerDao.save(customer);
  
  return new PetStoreCustomer(dbCustomer);
}
  
  
  @Transactional(readOnly = true)
  public List<PetStoreData> retrieveAllPetStores() {
    List<PetStore> petStores = petStoreDao.findAll();
    
    List<PetStoreData> result = new LinkedList<>();
    
    for(PetStore petStore : petStores) {
      PetStoreData psd = new PetStoreData(petStore);
      
      psd.getCustomers().clear();
      psd.getEmployees().clear();
      
      result.add(psd);
      }
    return result;
  }

  @Transactional(readOnly = true)
  public PetStoreData retrievePetStoreById(Long petStoreId) {
    return new PetStoreData(findPetStoreById(petStoreId));
    
  }

  @Transactional(readOnly = false)
  public void deletePetStoreById(Long petStoreId) {    
    PetStore petStore = findPetStoreById(petStoreId);
    petStoreDao.delete(petStore);
  }
  
  }