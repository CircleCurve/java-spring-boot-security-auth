package com.patrick.auth.services;

import com.patrick.auth.entities.House;
import com.patrick.auth.entities.User;
import com.patrick.auth.repositories.HouseRepository;
import com.patrick.auth.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AsyncService {
    private static final Logger logger = LoggerFactory.getLogger(AsyncService.class);

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private ApplicationContext applicationContext;

    public String asyncUpdate(){
        final AsyncService self = applicationContext.getBean(AsyncService.class);
        self.updateRecordInAsync();
        return "Success";
    }


    @Async
    public void updateRecordInAsync() {
        final AsyncService self = applicationContext.getBean(AsyncService.class);

        try {
            self.updateHouse();
        }catch (Exception e){
            self.updateRollbackHouse();
        }

    }

    public void updateHouse() {
        try {
            Thread.sleep(2000);
            House house = houseRepository.findByName("House 4");
            house.setName("House 44");
            houseRepository.save(house);

            logger.info("Success to save house", house);

            throw new RuntimeException("Success to save house but rollback.");
        }catch (Exception e){
            logger.error("Exception in updateRecord in Async :" , e);
            throw new RuntimeException(e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateRollbackHouse(){
        House house = houseRepository.findByName("House 3");
        house.setName("House 3 rollback by house 4");
        houseRepository.save(house);

        logger.info("Success to save rollback house", house);
    }
}
