package pet.store.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.service.PetStoreService;

@RestController
@Slf4j
public class PetStoreController {

  @Autowired
  private PetStoreService petStoreService;
  
  @PostMapping("/pet_store")
  @ResponseStatus(code = HttpStatus.CREATED)
  public PetStoreData createPetStore(@RequestBody PetStoreData petStoreData) {
    log.info("Creating pet store {}", petStoreData);
    
    return petStoreService.savePetStore(petStoreData);
    
  }
  
  @PutMapping("/pet_store/{petStoreId}")
  public PetStoreData updatePetStore(@PathVariable Long petStoreId, @RequestBody PetStoreData petStoreData) {
    
    petStoreData.setPetStoreId(petStoreId);
    log.info("Updating pet store {}", petStoreData);
    
    return petStoreService.savePetStore(petStoreData);
    
  }
  
  //Pet Store Employee Method
  
  @PostMapping("/pet_store/{petStoreId}/employee")
  @ResponseStatus(code = HttpStatus.CREATED)
  public PetStoreEmployee addEmployeeToPetStore(@PathVariable Long petStoreId,
      @RequestBody PetStoreEmployee petStoreEmployee) {
    log.info("...adding employee...", petStoreEmployee, petStoreId);
    
    return petStoreService.saveEmployee(petStoreId, petStoreEmployee);
    
  }
  
  //Pet Store Customer Method
 
  @PostMapping("/pet_store/{petStoreId}/customer")
  @ResponseStatus(code = HttpStatus.CREATED)
  public PetStoreCustomer addCustomerToPetStore(@PathVariable Long petStoreId,
      @RequestBody PetStoreCustomer petStoreCustomer) {
    log.info("...adding customer...", petStoreCustomer, petStoreId);
    
    return petStoreService.saveCustomer(petStoreId, petStoreCustomer);
}
  
  //list of all Pet Store Method
  
  @GetMapping
  public List<PetStoreData> retrieveAllPetStores(){
    log.info("Retrieving all pet stores");
    return petStoreService.retrieveAllPetStores();
    
  }
  
  //Retrieved Pet Store by ID Method
  @GetMapping("/pet_store/{petStoreId}")
  public PetStoreData retrievePetStoreById(@PathVariable Long petStoreId){
    log.info("Retrieving pet store with ID={}", petStoreId);
    return petStoreService.retrievePetStoreById(petStoreId);
  }
  @PostConstruct
  public void init() {
    System.out.println ("petstorecontroller initialized");
  }
  
  //Delete a pet store by id Method
  
  @DeleteMapping("/pet_store/{petStoreId}")
  public Map<String, String> deletePetStoreById(@PathVariable Long petStoreId){
    log.info("....deleting pet store", petStoreId);
    petStoreService.deletePetStoreById(petStoreId);
    Map<String, String> response = new HashMap<>();
    response.put("message", "pet store with Id "+ petStoreId + "was successfully deleted.");
    return response;
  }
}
  